package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_has_recipe", schema = "food_pilot", catalog = "")
@IdClass(MenuHasRecipeEntityPK.class)
public class MenuHasRecipeEntity {
    private int menuId;
    private int recipeId;
    private String domainName;

    @Id
    @Column(name = "menu_id")
    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Id
    @Column(name = "recipe_id")
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
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
        MenuHasRecipeEntity that = (MenuHasRecipeEntity) o;
        return menuId == that.menuId &&
                recipeId == that.recipeId &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(menuId, recipeId, domainName);
    }
}
