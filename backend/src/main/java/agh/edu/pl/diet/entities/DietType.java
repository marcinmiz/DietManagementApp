package agh.edu.pl.diet.entities;

import javax.persistence.*;

@Entity
@Table(name = "Diet_types")
public class DietType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dietTypeId;
    private String dietTypeName;
    private Double proteinCoefficient;
    private Double carbohydrateCoefficient;
    private Double FatCoefficient;

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

    public Double getProteinCoefficient() {
        return proteinCoefficient;
    }

    public void setProteinCoefficient(Double proteinCoefficient) {
        this.proteinCoefficient = proteinCoefficient;
    }

    public Double getCarbohydrateCoefficient() {
        return carbohydrateCoefficient;
    }

    public void setCarbohydrateCoefficient(Double carbohydrateCoefficient) {
        this.carbohydrateCoefficient = carbohydrateCoefficient;
    }

    public Double getFatCoefficient() {
        return FatCoefficient;
    }

    public void setFatCoefficient(Double fatCoefficient) {
        FatCoefficient = fatCoefficient;
    }
}
