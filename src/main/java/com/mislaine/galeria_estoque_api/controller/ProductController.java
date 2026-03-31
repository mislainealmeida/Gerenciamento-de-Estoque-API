package com.mislaine.galeria_estoque_api.controller;

import com.mislaine.galeria_estoque_api.dto.ProductDto;
import com.mislaine.galeria_estoque_api.model.Product;
import com.mislaine.galeria_estoque_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController //Diz ao Spring que aqui é a entrada web e que responde com JSON.
@RequestMapping("/api/products") // Define o endereço que o usuario deve acessar.
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //Cadastrar produto e responder que o processo deu certo.
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDto productDto) {
        Product savedProduct = productService.save(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    //Trazer a lista de todos os produtos cadastrados no BD
    @GetMapping //só atende pedidos get
    public ResponseEntity<Page<Product>> getAll(
            @RequestParam(defaultValue = "0") int page,//passa os parametros soltos
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateById(@PathVariable UUID id, @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @PutMapping("/sku/{sku}")
    public ResponseEntity<Product> updateBySku(@PathVariable String sku, @Valid @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateBySku(sku, dto));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getBySku(@PathVariable String sku) {
        return productService.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/name")
    public ResponseEntity<Page<Product>> searchByName(
            @RequestParam String name,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable // <-- Adicione isso!
    ) {
        return ResponseEntity.ok(productService.findByName(name, pageable));
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getByCategory(@PathVariable UUID categoryId, Pageable pageable) {
        return ResponseEntity.ok(productService.findByCategory(categoryId, pageable));
    }
    @GetMapping("/low-stock")//buscar produtos com estoque zerado
    public ResponseEntity<Page<Product>> getLowStock(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        Page<Product> lowStockPage = productService.getLowStockDashboard(pageable);
        return ResponseEntity.ok(lowStockPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/sku/{sku}")
    public ResponseEntity<Void> deleteBySku(@PathVariable String sku) {
        productService.deleteBySku(sku);
        return ResponseEntity.noContent().build();
    }
}