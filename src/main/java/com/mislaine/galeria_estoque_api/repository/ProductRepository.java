package com.mislaine.galeria_estoque_api.repository;

import com.mislaine.galeria_estoque_api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    //herda os codigos da JpaRepository<onde salvar, tipo da chave primária>
    Optional<Product> findBySku(String sku); // Faz a busca pelo SKU
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.stockAvailable = 0")
    Page<Product> findLowStock(Pageable pageable);//busca para estoque zerado

}
