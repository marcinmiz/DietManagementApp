package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class ProductSearchRequest {
    @NotBlank
    private String phrase;
    @NotBlank
    private String category;

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
