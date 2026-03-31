package com.mislaine.galeria_estoque_api.dto;


import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,

        String description
) {}