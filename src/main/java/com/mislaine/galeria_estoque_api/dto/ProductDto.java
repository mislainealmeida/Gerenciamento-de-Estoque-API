package com.mislaine.galeria_estoque_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto( //Usando o record não precisa de getter, setter e construtor.
                          @NotBlank String name,
                          String description,
                          @NotNull @Positive BigDecimal price,
                          @NotNull Integer stockAvailable,
                          @NotNull Integer minStockLevel,
                          @NotNull UUID categoryId // O front envia apenas o ID da categoria!
) {}