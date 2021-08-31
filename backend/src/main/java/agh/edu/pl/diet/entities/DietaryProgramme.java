package agh.edu.pl.diet.entities;

import javax.persistence.*;

@Entity
@Table(name = "DietaryProgramme")
public class DietaryProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dietaryProgrammeId;
    private String dietaryProgrammeName;
    @ManyToOne
    private User owner;
    private Integer dietaryProgrammeDays;

    public Long getDietaryProgrammeId() {
        return dietaryProgrammeId;
    }

    public void setDietaryProgrammeId(Long dietaryProgrammeId) {
        this.dietaryProgrammeId = dietaryProgrammeId;
    }

    public String getDietaryProgrammeName() {
        return dietaryProgrammeName;
    }

    public void setDietaryProgrammeName(String dietaryProgrammeName) {
        this.dietaryProgrammeName = dietaryProgrammeName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getDietaryProgrammeDays() {
        return dietaryProgrammeDays;
    }

    public void setDietaryProgrammeDays(Integer dietaryProgrammeDays) {
        this.dietaryProgrammeDays = dietaryProgrammeDays;
    }
}
