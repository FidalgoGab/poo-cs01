package br.com.fiapmotiva.controller;

import br.com.fiapmotiva.dto.TrechoRequestDTO;
import br.com.fiapmotiva.dto.TrechoResponseDTO;
import br.com.fiapmotiva.service.TrechoRodoviaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    private final TrechoRodoviaService service;

    public TrechoRodoviaController(TrechoRodoviaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TrechoResponseDTO>> listarTodos(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double nivelMinimo) {
        List<TrechoResponseDTO> trechos = service.listarTodos(tipo, nivelMinimo);
        return ResponseEntity.ok(trechos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrechoResponseDTO> buscarPorId(@PathVariable Long id) {
        TrechoResponseDTO trecho = service.buscarPorId(id);
        return ResponseEntity.ok(trecho);
    }

    @PostMapping
    public ResponseEntity<TrechoResponseDTO> criar(@Valid @RequestBody TrechoRequestDTO dto) {
        TrechoResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.idTrecho())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrechoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TrechoRequestDTO dto) {
        TrechoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
