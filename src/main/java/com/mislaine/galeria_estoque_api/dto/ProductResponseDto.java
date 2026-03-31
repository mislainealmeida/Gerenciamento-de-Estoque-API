package com.mislaine.galeria_estoque_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        String categoryName, // Em vez do objeto todo, passamos so o texto
        String imageUrl,
        boolean isAvailable // Podemos criar uma lógica: se stock > 0, true.
) {}
