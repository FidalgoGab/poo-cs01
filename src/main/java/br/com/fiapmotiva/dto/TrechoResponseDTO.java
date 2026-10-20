package br.com.fiapmotiva.dto;

public record TrechoResponseDTO(
        Long idTrecho,
        String nomeTrecho,
        Double kmInicial,
        Double kmFinal,
        Double extensaoKm,
        Double nivelVegetacaoCm,
        String tipoTrecho,
        Integer monitoravelIot,
        Long idEquipe,
        String nomeEquipe
) {
}
