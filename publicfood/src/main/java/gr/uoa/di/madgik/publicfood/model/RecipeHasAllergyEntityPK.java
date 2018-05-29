package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RecipeHasAllergyEntityPK implements Serializable {
    private int recipeId;
    private String recipeDomainName;
    private int allergyIdallergy;
    private String allergyDomainName;

    @Column(name = "recipe_id")
    @Id
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Column(name = "recipe_Domain_name")
    @Id
    public String getRecipeDomainName() {
        return recipeDomainName;
    }

    public void setRecipeDomainName(String recipeDomainName) {
        this.recipeDomainName = recipeDomainName;
    }

    @Column(name = "allergy_idallergy")
    @Id
    public int getAllergyIdallergy() {
        return allergyIdallergy;
    }

    public void setAllergyIdallergy(int allergyIdallergy) {
        this.allergyIdallergy = allergyIdallergy;
    }

    @Column(name = "allergy_Domain_name")
    @Id
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
        RecipeHasAllergyEntityPK that = (RecipeHasAllergyEntityPK) o;
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
