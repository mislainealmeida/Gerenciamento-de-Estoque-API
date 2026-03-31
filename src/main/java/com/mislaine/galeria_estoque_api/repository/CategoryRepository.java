package com.mislaine.galeria_estoque_api.repository;

import com.mislaine.galeria_estoque_api.model.Category;
import com.mislaine.galeria_estoque_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
        // O findById já existe "escondido" aqui dentro por herança
        Optional<Category> findByNameIgnoreCase(String name);
}
