package br.com.fiapmotiva.controller;

import br.com.fiapmotiva.dto.IntervencaoRequestDTO;
import br.com.fiapmotiva.dto.IntervencaoResponseDTO;
import br.com.fiapmotiva.service.IntervencaoOperacionalService;
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
@RequestMapping("/api/intervencoes")
public class IntervencaoOperacionalController {

    private final IntervencaoOperacionalService service;

    public IntervencaoOperacionalController(IntervencaoOperacionalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<IntervencaoResponseDTO>> listarTodas() {
        List<IntervencaoResponseDTO> intervencoes = service.listarTodas();
        return ResponseEntity.ok(intervencoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntervencaoResponseDTO> buscarPorId(@PathVariable Long id) {
        IntervencaoResponseDTO intervencao = service.buscarPorId(id);
        return ResponseEntity.ok(intervencao);
    }

    @PostMapping
    public ResponseEntity<IntervencaoResponseDTO> criar(@Valid @RequestBody IntervencaoRequestDTO dto) {
        IntervencaoResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.idIntervencao())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntervencaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody IntervencaoRequestDTO dto) {
        IntervencaoResponseDTO atualizada = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
