package agh.edu.pl.diet.entities;

import javax.persistence.*;

@Entity
@Table(name = "Diet_type")
public class DietType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dietTypeId;
    private String dietTypeName;

//    @JsonIgnore
//    @OneToMany(mappedBy = "diet_type", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
//    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
//    private Set<DietaryPreferences> dietaryPreferences = new HashSet<>();

    public Long getDietTypeId() {
        return dietTypeId;
    }

    public void setDietTypeId(Long dietTypeId) {
        this.dietTypeId = dietTypeId;
    }

    public String getDietTypeName() {
        return dietTypeName;
    }

    public void setDietTypeName(String dietTypeName) {
        this.dietTypeName = dietTypeName;
    }

//    public Set<DietaryPreferences> getDietaryPreferences() {
//        return dietaryPreferences;
//    }
//
//    public void setDietaryPreferences(Set<DietaryPreferences> dietaryPreferences) {
//        this.dietaryPreferences = dietaryPreferences;
//    }
}
