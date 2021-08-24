package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Nutrient")
public class Nutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long nutrientId;
    private String nutrientName;

    @JsonIgnore
    @OneToMany(mappedBy = "nutrient", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<ProductNutrient> products = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "nutrient", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesNutrient> dietaryPreferences = new HashSet<>();

    public Long getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(Long nutrientId) {
        this.nutrientId = nutrientId;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }

    public Set<ProductNutrient> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductNutrient> products) {
        this.products = products;
    }

    public void addProduct(ProductNutrient product) {
        products.add(product);
    }

    public Set<DietaryPreferencesNutrient> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(Set<DietaryPreferencesNutrient> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
}
