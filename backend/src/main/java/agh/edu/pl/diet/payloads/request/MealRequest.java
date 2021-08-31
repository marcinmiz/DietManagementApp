package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MealRequest {
    @NotBlank
    private String mealName;
    @NotBlank
    private String mealHourTime;
    @NotBlank
    private List<String> dailyMenu;
    @NotBlank
    private List<String> recipe;

}