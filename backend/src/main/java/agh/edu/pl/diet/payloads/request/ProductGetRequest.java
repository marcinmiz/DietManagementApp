package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class ProductGetRequest {
    @NotBlank
    private String productsGroup;
    @NotBlank
    private String phrase;
    @NotBlank
    private String category;

    public String getProductsGroup() {
        return productsGroup;
    }

    public void setProductsGroup(String productsGroup) {
        this.productsGroup = productsGroup;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
