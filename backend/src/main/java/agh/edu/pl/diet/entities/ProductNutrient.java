package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product_nutrients")
public class ProductNutrient {
    @JsonIgnore
    @Id
    @Column(name = "product_nutrient_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="nutrient_id", nullable=false)
    private Nutrient nutrient;

    @NotNull
    private Double quantity;

    public ProductNutrient() {}
}