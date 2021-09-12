package agh.edu.pl.diet.services.impl;

public enum Type {
    REGULAR(0.9, 4.0, 0.5),
    BODYCUILDING(1.5, 1.0, 0.5),
    KETOGENIC(1.0, 0.5, 1.5);

    private final double multiplicationCalculateProtein;
    private final double multiplicationCalculateCarbohydrates;
    private final double multiplicationCalculateFats;

    Type(double multiplicationCalculateProtein, double multiplicationCalculateCarbohydrates, double multiplicationCalculateFats) {
        this.multiplicationCalculateProtein = multiplicationCalculateProtein;
        this.multiplicationCalculateCarbohydrates = multiplicationCalculateCarbohydrates;
        this.multiplicationCalculateFats = multiplicationCalculateFats;
    }

    public double getMultiplicationCalculateProtein() {
        return multiplicationCalculateProtein;
    }

    public double getMultiplicationCalculateCarbohydrates() {
        return multiplicationCalculateCarbohydrates;
    }

    public double getMultiplicationCalculateFats() {
        return multiplicationCalculateFats;
    }
}