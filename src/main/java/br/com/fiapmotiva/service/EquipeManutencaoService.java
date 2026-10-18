package br.com.fiapmotiva.service;

import br.com.fiapmotiva.dto.EquipeRequestDTO;
import br.com.fiapmotiva.dto.EquipeResponseDTO;
import br.com.fiapmotiva.exception.BusinessException;
import br.com.fiapmotiva.exception.ResourceNotFoundException;
import br.com.fiapmotiva.model.EquipeManutencao;
import br.com.fiapmotiva.repository.EquipeManutencaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipeManutencaoService {

    private final EquipeManutencaoRepository repository;

    public EquipeManutencaoService(EquipeManutencaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EquipeResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipeResponseDTO buscarPorId(Long id) {
        EquipeManutencao equipe = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe com ID " + id + " nao encontrada."));
        return toResponseDTO(equipe);
    }

    @Transactional
    public EquipeResponseDTO criar(EquipeRequestDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BusinessException("O nome da equipe e obrigatorio.");
        }

        EquipeManutencao equipe = new EquipeManutencao();
        equipe.setNome(dto.nome().trim());
        equipe.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);

        EquipeManutencao salva = repository.save(equipe);
        return toResponseDTO(salva);
    }

    @Transactional
    public EquipeResponseDTO atualizar(Long id, EquipeRequestDTO dto) {
        EquipeManutencao equipe = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe com ID " + id + " nao encontrada."));

        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BusinessException("O nome da equipe e obrigatorio.");
        }

        equipe.setNome(dto.nome().trim());
        equipe.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);

        EquipeManutencao atualizada = repository.save(equipe);
        return toResponseDTO(atualizada);
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Equipe com ID " + id + " nao encontrada.");
        }
        repository.deleteById(id);
    }

    private EquipeResponseDTO toResponseDTO(EquipeManutencao entity) {
        return new EquipeResponseDTO(
                entity.getIdEquipe(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}
