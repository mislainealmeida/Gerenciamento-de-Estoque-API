package com.mislaine.galeria_estoque_api.controller;

import com.mislaine.galeria_estoque_api.dto.CategoryDto;
import com.mislaine.galeria_estoque_api.model.Category;
import com.mislaine.galeria_estoque_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
//Controller conversa com a internet.
@RestController // Define que esta classe é um ponto de entrada da API
@RequestMapping("/api/categories") // Define o endereço (URL) base
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Metodo para Cadastrar (POST)
    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryDto dto) { // Usa o Dto
        Category savedCategory = categoryService.save(dto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Metodo para Listar Todas as categorias (GET)
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable UUID id, @Valid @RequestBody CategoryDto dto) { // Usa o Dto
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}