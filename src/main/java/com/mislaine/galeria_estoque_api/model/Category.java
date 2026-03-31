package com.mislaine.galeria_estoque_api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//BD decide o próximo número
    private UUID id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Um "espelho" para o Java saber quais produtos estão nesta categoria
    @OneToMany(mappedBy = "category")// um para muitos, uma categoria para vários produtos, diz que a regra já esta em category.
    @JsonIgnoreProperties("category") // não repetir as categorias
    private List<Product> products; // uma lista de produtos dentro de categorias

}