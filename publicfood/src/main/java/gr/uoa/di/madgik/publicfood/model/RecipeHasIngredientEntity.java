package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "recipe_has_ingredient", schema = "food_pilot", catalog = "")
@IdClass(RecipeHasIngredientEntityPK.class)
public class RecipeHasIngredientEntity {
    private int id;
    private int recipeId;
    private int ingredientId;
    private Double ingredientQuantity;
    private String domainName;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "recipe_id")
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Basic
    @Column(name = "ingredient_id")
    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Basic
    @Column(name = "ingredient_quantity")
    public Double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(Double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    @Id
    @Column(name = "Domain_name")
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeHasIngredientEntity that = (RecipeHasIngredientEntity) o;
        return id == that.id &&
                recipeId == that.recipeId &&
                ingredientId == that.ingredientId &&
                Objects.equals(ingredientQuantity, that.ingredientQuantity) &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, recipeId, ingredientId, ingredientQuantity, domainName);
    }
}
