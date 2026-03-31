package com.mislaine.galeria_estoque_api.notification;

import com.mislaine.galeria_estoque_api.model.Product;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(NotificationService.class); // Logger serve falar com você através de arquivos de texto ou do console, contando o que está acontecendo.

    public void checkProductCuratorship(Product product) {
        // Alerta se o quadro estiver sem URL de imagem
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            logger.warn("📸 ATENÇÃO CURADORIA: O quadro '{}' foi cadastrado sem foto. " +
                    "O item não ficará visível na vitrine virtual!", product.getName());
        }

        // Alerta para obras de alto valor
        if (product.getPrice().compareTo(new java.math.BigDecimal("1000")) > 0) {
            logger.info("💎 ITEM DE LUXO: Nova obra de alto valor adicionada ao acervo: {} - R$ {}",
                    product.getName(), product.getPrice());
        }
    }
}
