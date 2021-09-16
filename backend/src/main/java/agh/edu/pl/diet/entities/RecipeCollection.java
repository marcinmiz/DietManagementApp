package agh.edu.pl.diet.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes_collections")
public class RecipeCollection {

    @Id
    @Column(name = "user_recipe_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinColumn(name="user_id", nullable=false)
    private User recipeCollector;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Recipes> recipeCollection;

    public RecipeCollection() {
    }

    public RecipeCollection(User recipeCollector) {
        this.recipeCollector = recipeCollector;
        this.recipeCollection = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRecipeCollector() {
        return recipeCollector;
    }

    public void setRecipeCollector(User recipeCollector) {
        this.recipeCollector = recipeCollector;
    }

    public List<Recipes> getRecipeCollection() {
        return recipeCollection;
    }

    public void setRecipeCollection(List<Recipes> recipeCollection) {
        this.recipeCollection = recipeCollection;
    }

    public void addRecipeToCollection(Recipes recipe) {
        this.recipeCollection.add(recipe);
    }
}
