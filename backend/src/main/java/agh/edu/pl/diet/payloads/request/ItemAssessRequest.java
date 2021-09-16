package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class ItemAssessRequest {
    @NotBlank
    private Long itemId;
    @NotBlank
    private String assessment;
    @NotBlank
    private String rejectExplanation;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long productId) {
        this.itemId = productId;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getRejectExplanation() {
        return rejectExplanation;
    }

    public void setRejectExplanation(String rejectExplanation) {
        this.rejectExplanation = rejectExplanation;
    }
}
