package br.com.fiapmotiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record TrechoRequestDTO(
        @NotBlank(message = "O nome do trecho e obrigatorio.")
        @Size(max = 120, message = "O nome do trecho deve ter no maximo 120 caracteres.")
        String nomeTrecho,

        @NotNull(message = "O quilometro inicial e obrigatorio.")
        @PositiveOrZero(message = "O quilometro inicial deve ser maior ou igual a zero.")
        Double kmInicial,

        @NotNull(message = "O quilometro final e obrigatorio.")
        @Positive(message = "O quilometro final deve ser maior que zero.")
        Double kmFinal,

        @NotNull(message = "O nivel de vegetacao e obrigatorio.")
        @PositiveOrZero(message = "O nivel de vegetacao deve ser maior ou igual a zero.")
        Double nivelVegetacaoCm,

        @NotBlank(message = "O tipo de trecho e obrigatorio.")
        @Pattern(regexp = "^(SECO|UMIDO)$", message = "O tipo de trecho deve ser SECO ou UMIDO.")
        String tipoTrecho,

        Integer monitoravelIot,

        Long idEquipe
) {
}
