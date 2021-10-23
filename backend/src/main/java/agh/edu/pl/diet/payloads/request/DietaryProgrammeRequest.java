package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class DietaryProgrammeRequest {
    @NotBlank
    private String DietaryProgrammeName;
    @NotBlank
    private Long preferenceId;
    @NotBlank
    private Integer dietaryProgrammeDays;

    public String getDietaryProgrammeName() {
        return DietaryProgrammeName;
    }

    public void setDietaryProgrammeName(String dietaryProgrammeName) {
        DietaryProgrammeName = dietaryProgrammeName;
    }

    public Long getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(Long preferenceId) {
        this.preferenceId = preferenceId;
    }

    public Integer getDietaryProgrammeDays() {
        return dietaryProgrammeDays;
    }

    public void setDietaryProgrammeDays(Integer dietaryProgrammeDays) {
        this.dietaryProgrammeDays = dietaryProgrammeDays;
    }
}
