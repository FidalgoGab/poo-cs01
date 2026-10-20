package br.com.fiapmotiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipeRequestDTO(
        @NotBlank(message = "O nome da equipe e obrigatorio.")
        @Size(max = 80, message = "O nome da equipe deve ter no maximo 80 caracteres.")
        String nome,

        @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres.")
        String descricao
) {
}
