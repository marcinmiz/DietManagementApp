package agh.edu.pl.diet.payloads.response;

public class SuitabilityRecipeResponse {

    private Double preferenceCalories;
    private Boolean suitable;
    private String notSuitableExplanation;

    public SuitabilityRecipeResponse(Boolean suitable, String notSuitableExplanation) {
        this.suitable = suitable;
        this.notSuitableExplanation = notSuitableExplanation;
    }

    public Double getPreferenceCalories() {
        return preferenceCalories;
    }

    public void setPreferenceCalories(Double preferenceCalories) {
        this.preferenceCalories = preferenceCalories;
    }

    public Boolean getSuitable() {
        return suitable;
    }

    public String getNotSuitableExplanation() {
        return notSuitableExplanation;
    }
}
