package agh.edu.pl.diet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;
    private String name;
    private String surname;
    @JsonIgnore
    private String password;
    @JsonIgnore
    @Transient
    private String passwordConfirmation;
    private String username;
    private String email;
    @ManyToOne
    private Role role;
    private String creationDate;
    @Transient
    private List<Pair<String, Float>> weights = new ArrayList<>();
    @OneToOne
    private DietaryProgramme currentDietaryProgramme = null;
    private String dietaryProgrammeStartDate = null;
    private Integer currentDietaryProgrammeDay = null;
    @JsonIgnore
    private String resetPasswordToken;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirm) {
        this.passwordConfirmation = passwordConfirm;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Pair<String, Float>> getWeights() {
        return weights;
    }

    public void setWeights(List<Pair<String, Float>> weights) {
        this.weights = weights;
    }

    public void addNewWeight(Pair<String, Float> weight) {
        this.weights.add(weight);
    }

    public DietaryProgramme getCurrentDietaryProgramme() {
        return currentDietaryProgramme;
    }

    public void setCurrentDietaryProgramme(DietaryProgramme currentDietaryProgramme) {
        this.currentDietaryProgramme = currentDietaryProgramme;
    }

    public String getDietaryProgrammeStartDate() {
        return dietaryProgrammeStartDate;
    }

    public void setDietaryProgrammeStartDate(String dietaryProgrammeStartDate) {
        this.dietaryProgrammeStartDate = dietaryProgrammeStartDate;
    }

    public Integer getCurrentDietaryProgrammeDay() {
        return currentDietaryProgrammeDay;
    }

    public void setCurrentDietaryProgrammeDay(Integer currentDietaryProgrammeDay) {
        this.currentDietaryProgrammeDay = currentDietaryProgrammeDay;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    //    public double getTargetWeight() {
//        return targetWeight;
//    }
//
//    public void setTargetWeight(double targetWeight) {
//        this.targetWeight = targetWeight;
//    }

    //    public void setTargetWeight(double newTargetWeight) {    //newTarget<0 is handled in the consutructor
//        targetWeight = newTargetWeight;
//        setChanged();//Makes change true so that it will notify observers
//        notifyObservers(this);//Passes current user as a parameter to Observer in this case Diet
        //So that Diet can access the User object.

}