package agh.edu.pl.diet.entities;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "weights")
public class Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long weightId;
    private Double weightValue;
    private String measureDate;
    @ManyToOne
    private User measurer;

    public Weight() {
    }

    public Weight(Double weightValue, User measurer) {
        this.weightValue = weightValue;
        this.measurer = measurer;
        this.measureDate = Calendar.getInstance().toInstant().toString();
    }

    public Long getWeightId() {
        return weightId;
    }

    public void setWeightId(Long weightId) {
        this.weightId = weightId;
    }

    public Double getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(Double weightValue) {
        this.weightValue = weightValue;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public User getMeasurer() {
        return measurer;
    }

    public void setMeasurer(User measurer) {
        this.measurer = measurer;
    }
}
