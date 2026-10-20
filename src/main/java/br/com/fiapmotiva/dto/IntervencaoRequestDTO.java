package br.com.fiapmotiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IntervencaoRequestDTO(
        @NotBlank(message = "O nome da intervencao e obrigatorio.")
        @Size(max = 50, message = "O nome da intervencao deve ter no maximo 50 caracteres.")
        String nome,

        @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres.")
        String descricao
) {
}
