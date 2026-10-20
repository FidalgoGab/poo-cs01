package br.com.fiapmotiva.controller;

import br.com.fiapmotiva.dto.RelatorioResponseDTO;
import br.com.fiapmotiva.service.RelatorioPrioridadeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    private final RelatorioPrioridadeService service;

    public RelatorioPrioridadeController(RelatorioPrioridadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RelatorioResponseDTO> gerarRelatorio() {
        RelatorioResponseDTO relatorio = service.gerarRelatorio();
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorio);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioResponseDTO>> listarHistorico() {
        List<RelatorioResponseDTO> historico = service.listarHistorico();
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<RelatorioResponseDTO>> consultarPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<RelatorioResponseDTO> relatorios = service.consultarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(relatorios);
    }
}
