package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RecipeRequest {

    @NotBlank
    private String productName;
    @NotBlank
    private Integer calories;
    @NotBlank
    private String category;
    @NotBlank
    private List<String> nutrients;


}
