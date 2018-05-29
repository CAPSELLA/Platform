package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "translation_recipe", schema = "food_pilot", catalog = "")
public class TranslationRecipeEntity {
    private int id;
    private String language;
    private String text;
    private int recipeId1;
    private String recipeDomainName1;
    private int recipe_type;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "recipe_id1")
    public int getRecipeId1() {
        return recipeId1;
    }

    public void setRecipeId1(int recipeId1) {
        this.recipeId1 = recipeId1;
    }

    @Basic
    @Column(name = "recipe_Domain_name1")
    public String getRecipeDomainName1() {
        return recipeDomainName1;
    }

    public void setRecipeDomainName1(String recipeDomainName1) {
        this.recipeDomainName1 = recipeDomainName1;
    }

    @Basic
    @Column(name = "recipe_type")
    public int getRecipeType() {
        return recipe_type;
    }

    public void setRecipeType(int recipeType) {
        this.recipe_type = recipeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationRecipeEntity that = (TranslationRecipeEntity) o;
        return id == that.id &&
                recipeId1 == that.recipeId1 &&
                Objects.equals(language, that.language) &&
                Objects.equals(text, that.text) &&
                Objects.equals(recipeDomainName1, that.recipeDomainName1)&&
                recipe_type == that.recipe_type ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, language, text, recipeId1, recipeDomainName1, recipe_type);
    }
}
