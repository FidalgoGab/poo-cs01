package br.com.fiapmotiva.service;

import br.com.fiapmotiva.dto.TrechoRequestDTO;
import br.com.fiapmotiva.dto.TrechoResponseDTO;
import br.com.fiapmotiva.exception.BusinessException;
import br.com.fiapmotiva.exception.ResourceNotFoundException;
import br.com.fiapmotiva.model.EquipeManutencao;
import br.com.fiapmotiva.model.TrechoRodovia;
import br.com.fiapmotiva.repository.EquipeManutencaoRepository;
import br.com.fiapmotiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrechoRodoviaService {

    private final TrechoRodoviaRepository trechoRepository;
    private final EquipeManutencaoRepository equipeRepository;

    public TrechoRodoviaService(TrechoRodoviaRepository trechoRepository,
                                EquipeManutencaoRepository equipeRepository) {
        this.trechoRepository = trechoRepository;
        this.equipeRepository = equipeRepository;
    }

    @Transactional(readOnly = true)
    public List<TrechoResponseDTO> listarTodos(String tipoTrecho, Double nivelMinimo) {
        List<TrechoRodovia> trechos;

        if (tipoTrecho != null && !tipoTrecho.trim().isEmpty()) {
            trechos = trechoRepository.findByTipoTrecho(tipoTrecho.trim().toUpperCase());
        } else if (nivelMinimo != null) {
            trechos = trechoRepository.findByNivelVegetacaoCmGreaterThanEqual(nivelMinimo);
        } else {
            trechos = trechoRepository.findAll();
        }

        return trechos.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrechoResponseDTO buscarPorId(Long id) {
        TrechoRodovia trecho = trechoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trecho com ID " + id + " nao encontrado."));
        return toResponseDTO(trecho);
    }

    @Transactional
    public TrechoResponseDTO criar(TrechoRequestDTO dto) {
        validarRegrasTrecho(dto);

        TrechoRodovia trecho = new TrechoRodovia();
        aplicarDados(trecho, dto);

        TrechoRodovia salvo = trechoRepository.save(trecho);
        return toResponseDTO(salvo);
    }

    @Transactional
    public TrechoResponseDTO atualizar(Long id, TrechoRequestDTO dto) {
        TrechoRodovia trecho = trechoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trecho com ID " + id + " nao encontrado."));

        validarRegrasTrecho(dto);
        aplicarDados(trecho, dto);

        TrechoRodovia atualizado = trechoRepository.save(trecho);
        return toResponseDTO(atualizado);
    }

    @Transactional
    public void remover(Long id) {
        if (!trechoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trecho com ID " + id + " nao encontrado.");
        }
        trechoRepository.deleteById(id);
    }

    private void validarRegrasTrecho(TrechoRequestDTO dto) {
        if (dto.kmFinal() <= dto.kmInicial()) {
            throw new BusinessException("O quilometro final deve ser estritamente maior que o quilometro inicial.");
        }

        if (dto.nivelVegetacaoCm() < 0) {
            throw new BusinessException("O nivel de vegetacao nao pode ser negativo.");
        }
    }

    private void aplicarDados(TrechoRodovia trecho, TrechoRequestDTO dto) {
        trecho.setNomeTrecho(dto.nomeTrecho().trim());
        trecho.setKmInicial(dto.kmInicial());
        trecho.setKmFinal(dto.kmFinal());
        trecho.setNivelVegetacaoCm(dto.nivelVegetacaoCm());
        trecho.setTipoTrecho(dto.tipoTrecho().trim().toUpperCase());
        trecho.setMonitoravelIot(dto.monitoravelIot() != null ? dto.monitoravelIot() : 0);

        if (dto.idEquipe() != null) {
            EquipeManutencao equipe = equipeRepository.findById(dto.idEquipe())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipe com ID " + dto.idEquipe() + " informada nao existe."));
            trecho.setEquipe(equipe);
        } else {
            trecho.setEquipe(null);
        }
    }

    public TrechoResponseDTO toResponseDTO(TrechoRodovia entity) {
        Double extensao = (entity.getKmFinal() != null && entity.getKmInicial() != null)
                ? entity.getKmFinal() - entity.getKmInicial()
                : 0.0;

        Long idEquipe = entity.getEquipe() != null ? entity.getEquipe().getIdEquipe() : null;
        String nomeEquipe = entity.getEquipe() != null ? entity.getEquipe().getNome() : null;

        return new TrechoResponseDTO(
                entity.getIdTrecho(),
                entity.getNomeTrecho(),
                entity.getKmInicial(),
                entity.getKmFinal(),
                extensao,
                entity.getNivelVegetacaoCm(),
                entity.getTipoTrecho(),
                entity.getMonitoravelIot(),
                idEquipe,
                nomeEquipe
        );
    }
}
