package br.com.fiapmotiva.service;

import br.com.fiapmotiva.dto.IntervencaoRequestDTO;
import br.com.fiapmotiva.dto.IntervencaoResponseDTO;
import br.com.fiapmotiva.exception.BusinessException;
import br.com.fiapmotiva.exception.ResourceNotFoundException;
import br.com.fiapmotiva.model.IntervencaoOperacional;
import br.com.fiapmotiva.repository.IntervencaoOperacionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IntervencaoOperacionalService {

    private final IntervencaoOperacionalRepository repository;

    public IntervencaoOperacionalService(IntervencaoOperacionalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<IntervencaoResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public IntervencaoResponseDTO buscarPorId(Long id) {
        IntervencaoOperacional intervencao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervencao com ID " + id + " nao encontrada."));
        return toResponseDTO(intervencao);
    }

    @Transactional
    public IntervencaoResponseDTO criar(IntervencaoRequestDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BusinessException("O nome da intervencao e obrigatorio.");
        }

        String nomeNormalizado = dto.nome().trim().toUpperCase();
        if (repository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new BusinessException("Ja existe uma intervencao cadastrada com o nome: " + nomeNormalizado);
        }

        IntervencaoOperacional intervencao = new IntervencaoOperacional();
        intervencao.setNome(nomeNormalizado);
        intervencao.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);

        IntervencaoOperacional salva = repository.save(intervencao);
        return toResponseDTO(salva);
    }

    @Transactional
    public IntervencaoResponseDTO atualizar(Long id, IntervencaoRequestDTO dto) {
        IntervencaoOperacional intervencao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervencao com ID " + id + " nao encontrada."));

        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BusinessException("O nome da intervencao e obrigatorio.");
        }

        String nomeNormalizado = dto.nome().trim().toUpperCase();
        repository.findByNomeIgnoreCase(nomeNormalizado).ifPresent(existente -> {
            if (!existente.getIdIntervencao().equals(id)) {
                throw new BusinessException("Ja existe outra intervencao cadastrada com o nome: " + nomeNormalizado);
            }
        });

        intervencao.setNome(nomeNormalizado);
        intervencao.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);

        IntervencaoOperacional atualizada = repository.save(intervencao);
        return toResponseDTO(atualizada);
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Intervencao com ID " + id + " nao encontrada.");
        }
        repository.deleteById(id);
    }

    private IntervencaoResponseDTO toResponseDTO(IntervencaoOperacional entity) {
        return new IntervencaoResponseDTO(
                entity.getIdIntervencao(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}
