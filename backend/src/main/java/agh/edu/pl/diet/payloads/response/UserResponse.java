package agh.edu.pl.diet.payloads.response;

import agh.edu.pl.diet.entities.Role;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {

    private Long userId;
    private String name;
    private String surname;
    private String username;
    private Role role;
    private String creationDate;
    private List<Pair<String, Float>> weights = new ArrayList<>();

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

    public List<Pair<String, Float>> getWeights() {
        return weights;
    }

    public void setWeights(List<Pair<String, Float>> weights) {
        this.weights = weights;
    }

    public void addNewWeight(Pair<String, Float> weight) {
        this.weights.add(weight);
    }
}
