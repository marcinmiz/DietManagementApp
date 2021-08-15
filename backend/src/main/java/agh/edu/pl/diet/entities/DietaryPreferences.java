package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Dietary_preferences")
public class DietaryPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dietaryPreferenceId;
    private String dietaryPreferenceName;
    private Integer totalCalories;
    @ManyToOne
    private User owner;
    @ManyToOne
    private Category dietType;

    @OneToMany(mappedBy = "dietary_preference", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesNutrient> nutrients = new HashSet<>();

    private String creationDate = null;

    private Integer targetWeight;

    public Long getDietaryPreferenceId() {
        return dietaryPreferenceId;
    }

    public void setDietaryPreferenceId(Long dietaryPreferenceId) {
        this.dietaryPreferenceId = dietaryPreferenceId;
    }

    public String getDietaryPreferenceName() {
        return dietaryPreferenceName;
    }

    public void setDietaryPreferenceName(String dietaryPreferenceName) {
        this.dietaryPreferenceName = dietaryPreferenceName;
    }

    public Integer getCalories() {
        return totalCalories;
    }

    public void setCalories(Integer calories) {
        this.totalCalories = calories;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getDietType() {
        return dietType;
    }

    public void setDietType(Category dietType) {
        this.dietType = dietType;
    }

    public Set<DietaryPreferencesNutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Set<DietaryPreferencesNutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Integer targetWeight) {
        this.targetWeight = targetWeight;
    }
}
