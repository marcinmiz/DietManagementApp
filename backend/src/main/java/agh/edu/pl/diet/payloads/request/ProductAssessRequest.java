package agh.edu.pl.diet.payloads.request;

import javax.validation.constraints.NotBlank;

public class ProductAssessRequest {
    @NotBlank
    private Long productId;
    @NotBlank
    private String assessment;
    @NotBlank
    private String rejectExplanation;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
