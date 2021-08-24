package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Dietary_preferences_nutrient")
public class DietaryPreferencesNutrient {

    @JsonIgnore
    @Id
    @Column(name = "dietary_preferences_nutrient_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="dietary_preferences_id", nullable=false)
    private DietaryPreferences dietaryPreferences;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="nutrient_id", nullable=false)
    private Nutrient nutrient;

    @NotNull
    private Double nutrientAmount;

    public DietaryPreferencesNutrient() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DietaryPreferences getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(DietaryPreferences dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public void setNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }

    public Double getNutrientAmount() {
        return nutrientAmount;
    }

    public void setNutrientAmount(Double nutrientAmount) {
        this.nutrientAmount = nutrientAmount;
    }
}
