package br.com.fiapmotiva.controller;

import br.com.fiapmotiva.dto.EquipeRequestDTO;
import br.com.fiapmotiva.dto.EquipeResponseDTO;
import br.com.fiapmotiva.service.EquipeManutencaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    private final EquipeManutencaoService service;

    public EquipeManutencaoController(EquipeManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EquipeResponseDTO>> listarTodas() {
        List<EquipeResponseDTO> equipes = service.listarTodas();
        return ResponseEntity.ok(equipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResponseDTO> buscarPorId(@PathVariable Long id) {
        EquipeResponseDTO equipe = service.buscarPorId(id);
        return ResponseEntity.ok(equipe);
    }

    @PostMapping
    public ResponseEntity<EquipeResponseDTO> criar(@Valid @RequestBody EquipeRequestDTO dto) {
        EquipeResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.idEquipe())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipeRequestDTO dto) {
        EquipeResponseDTO atualizada = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
