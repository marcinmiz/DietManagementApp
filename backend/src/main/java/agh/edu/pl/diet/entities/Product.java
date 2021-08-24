package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private Integer calories;
    @ManyToOne
    private User owner;
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<ProductNutrient> nutrients = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Set<DietaryPreferencesProduct> dietaryPreferences = new HashSet<>();

    private String creationDate = null;

    private String approvalStatus = "pending";
    private String assessmentDate = null;
    private String rejectExplanation = "";

    private Boolean productFavourite = false;

    @Transient
    private String productImage;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Set<ProductNutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(Set<ProductNutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public void addNutrient(ProductNutrient productNutrient) {
        nutrients.add(productNutrient);
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getProductFavourite() {
        return productFavourite;
    }

    public void setProductFavourite(Boolean productFavourite) {
        this.productFavourite = productFavourite;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRejectExplanation() {
        return rejectExplanation;
    }

    public void setRejectExplanation(String rejectExplanation) {
        this.rejectExplanation = rejectExplanation;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public Set<DietaryPreferencesProduct> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(Set<DietaryPreferencesProduct> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
}