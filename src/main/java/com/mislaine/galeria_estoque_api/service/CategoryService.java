package com.mislaine.galeria_estoque_api.service;

import com.mislaine.galeria_estoque_api.dto.CategoryDto;
import com.mislaine.galeria_estoque_api.model.Category;
import com.mislaine.galeria_estoque_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
//Service conversa com o banco de dados

@Service // Avisa ao Spring que esta classe contém a lógica de negócio
@RequiredArgsConstructor // O Lombok cria o construtor para injetar o Repository automaticamente
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //Método para Salvar uma nova categoria
    @Transactional // Garante que, se algo der errado, o banco não salve dados incompletos
    public Category save(CategoryDto dto) { // Alterado para receber DTO
        Category category = new Category();
        category.setName(dto.name().toUpperCase().trim());
        category.setDescription(dto.description());
        return categoryRepository.save(category);
    }

    // Metodo para Listar todas as categorias
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Busca por nome
    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Categoria '" + name + "' não encontrada. Verifique a grafia!"));
    }

    //Busca por Id
    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria '" + id + "' não encontrada. Verifique a grafia!"));
    }

    @Transactional
    public Category update(UUID id, CategoryDto dto) { // Alterado para receber DTO
        // 1. Busca a categoria original
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // 2. Atualiza os dados (geralmente nome e descrição) usando sintaxe de Record
        category.setName(dto.name().toUpperCase().trim());
        category.setDescription(dto.description());

        // 3. Salva a alteração
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(UUID id) {
        // 1. Busca para ter certeza que existe
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // 2. Deletar
        categoryRepository.delete(category);
    }
}
