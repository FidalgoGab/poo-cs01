package br.com.fiapmotiva.dto;

import java.time.LocalDate;

public record RelatorioResponseDTO(
        Long idRelatorio,
        Integer qtUrgente,
        Integer qtCritico,
        Integer qtAtencao,
        Integer qtNormal,
        Integer totalTrechosAnalisados,
        String resumo,
        LocalDate dataGeracao
) {
}
