package com.mislaine.galeria_estoque_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity //Transforma sua classe Java em uma Tabela no PostgreSQL.
@Table(name = "tb_products") // Nomeia a tabela
@Data //Cria sozinho os métodos get e set

public class Product {
    @Id // Marca qual é a chave primária (única)
    @GeneratedValue(strategy = GenerationType.AUTO) //Gera o código sozinho

    @Column(name = "product_id", updatable = false, nullable = false)  //Valor não altera e o campo é obrigatório
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)// Não pode ser nulo, impede duplicidade, qtde máxima de letras
    private String sku;

    @NotBlank(message = "O nome do produto não pode estar em branco")//Evitando erros
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")// Defini o tipo de dado "raiz" no BD.
    private String description;

    @NotNull(message = "O preço é obrigatório")//cuidando dos erros.
    @Positive(message = "O preço deve ser um valor positivo")//Evitando erros
    @Column(nullable = false, precision = 10, scale = 2)//Máximo 10 dígitos, 2 casas decimais
    private BigDecimal price;

    @Min(value = 0, message = "O estoque não pode ser negativo")//Evitando erros
    private Integer stockAvailable;

    private Integer minStockLevel;
    private String imageUrl;
    private Boolean status = true;
    private BigDecimal weight;
    private String height;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @ManyToOne // Cria a ligação entre o Produto e a Categoria. Muitos produtos para uma categoria
    @JoinColumn(name = "category_id") //É a Chave Estrangeira (que liga produtos e categoria)
    @JsonIgnoreProperties("products") // não repetir os produtos
    private Category category;

    @PrePersist//O sistema deve executar a regra em um milésimo de segundo antes de um novo registro ser salvo
    public void generateSku() {
        String prefix = "GEN-"; // Padrao caso algo dê errado
        if (this.category != null && this.category.getName() != null) {
            String catName = this.category.getName();
            prefix = (catName.length() >= 3)
                    ? catName.substring(0, 3).toUpperCase() + "-"
                    : catName.toUpperCase() + "-";
        }
        String namePart = (name != null && name.length() >= 3)
                ? name.substring(0, 3).toUpperCase()
                : "OBRA";

        String uniquePart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        this.sku = prefix + namePart + "-" + uniquePart;
    }

    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
