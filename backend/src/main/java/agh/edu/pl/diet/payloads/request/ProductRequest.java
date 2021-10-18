package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ProductRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private Double calories;
    @NotBlank
    private String category;
    @NotBlank
    private List<String> nutrients;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<String> nutrients) {
        this.nutrients = nutrients;
    }
}
