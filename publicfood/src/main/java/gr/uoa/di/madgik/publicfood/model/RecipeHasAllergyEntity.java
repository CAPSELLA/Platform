package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "recipe_has_allergy", schema = "food_pilot", catalog = "")
@IdClass(RecipeHasAllergyEntityPK.class)
public class RecipeHasAllergyEntity {
    private int recipeId;
    private String recipeDomainName;
    private int allergyIdallergy;
    private String allergyDomainName;

    @Id
    @Column(name = "recipe_id")
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Id
    @Column(name = "recipe_Domain_name")
    public String getRecipeDomainName() {
        return recipeDomainName;
    }

    public void setRecipeDomainName(String recipeDomainName) {
        this.recipeDomainName = recipeDomainName;
    }

    @Id
    @Column(name = "allergy_idallergy")
    public int getAllergyIdallergy() {
        return allergyIdallergy;
    }

    public void setAllergyIdallergy(int allergyIdallergy) {
        this.allergyIdallergy = allergyIdallergy;
    }

    @Id
    @Column(name = "allergy_Domain_name")
    public String getAllergyDomainName() {
        return allergyDomainName;
    }

    public void setAllergyDomainName(String allergyDomainName) {
        this.allergyDomainName = allergyDomainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeHasAllergyEntity that = (RecipeHasAllergyEntity) o;
        return recipeId == that.recipeId &&
                allergyIdallergy == that.allergyIdallergy &&
                Objects.equals(recipeDomainName, that.recipeDomainName) &&
                Objects.equals(allergyDomainName, that.allergyDomainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(recipeId, recipeDomainName, allergyIdallergy, allergyDomainName);
    }
}
