package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class RecipeSearchRequest {

    @NotBlank
    private String phrase;
    @NotBlank
    private String category;
}
