package com.mislaine.galeria_estoque_api.service;

import com.mislaine.galeria_estoque_api.dto.ProductDto;
import com.mislaine.galeria_estoque_api.model.Category;
import com.mislaine.galeria_estoque_api.model.Product;
import com.mislaine.galeria_estoque_api.notification.NotificationService;
import com.mislaine.galeria_estoque_api.repository.CategoryRepository;
import com.mislaine.galeria_estoque_api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service // Avisa ao Spring que esta classe contém a lógica de negócio
@RequiredArgsConstructor // O Lombok cria o construtor para injetar o Repository automaticamente
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    @Transactional // 0 ou 80 (não salva produtos com erro)
    public Product save(ProductDto dto) {
        // 1. Buscamos a categoria pelo ID que veio no DTO
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // 2. Criamos a Entity Product a partir dos dados do DTO
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockAvailable(dto.stockAvailable());
        product.setMinStockLevel(dto.minStockLevel());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        this.notificationService.checkProductCuratorship(savedProduct);
        return savedProduct;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public Page<Product> findByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Product> findByCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> getLowStockDashboard(Pageable pageable) { //busca para estoque zerado
        return productRepository.findLowStock(pageable);
    }

    // Metodo para atualizar
    @Transactional
    public Product update(UUID id, ProductDto productDto) {
        // 1. Busca o produto. Se não achar, lança erro.
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        // 2. Atualiza os dados com o que veio da internet (Usando sintaxe de Record)
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setDescription(productDto.description());
        product.setStockAvailable(productDto.stockAvailable());
        product.setMinStockLevel(productDto.minStockLevel());

        // 3. Atualiza categoria
        // Busca a categoria no BD, troca o vínculo (ID) e salva
        if (productDto.categoryId() != null) {
            Category category = categoryRepository.findById(productDto.categoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        this.notificationService.checkProductCuratorship(updatedProduct);

        return updatedProduct;
    }

    @Transactional
    public Product updateBySku(String sku, ProductDto productDto) {
        // 1. Procura o produto pelo SKU
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Produto com SKU " + sku + " não encontrado"));

        // 2. Atualiza os campos (exceto o ID e o SKU, que são fixos)
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setDescription(productDto.description());
        product.setStockAvailable(productDto.stockAvailable());
        product.setMinStockLevel(productDto.minStockLevel());

        // Atualiza categoria se enviada
        if (productDto.categoryId() != null) {
            Category category = categoryRepository.findById(productDto.categoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        this.notificationService.checkProductCuratorship(updatedProduct);

        return updatedProduct;
    }

    // Metodo para deletar
    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        productRepository.delete(product);
    }

    @Transactional
    public void deleteBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Produto com SKU " + sku + " não encontrado"));
        productRepository.delete(product);
    }
}