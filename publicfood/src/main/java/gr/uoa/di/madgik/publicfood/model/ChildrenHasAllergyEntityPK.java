package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ChildrenHasAllergyEntityPK implements Serializable {
    private int childrenIdchildren;
    private int allergyIdallergy;

    @Column(name = "children_idchildren")
    @Id
    public int getChildrenIdchildren() {
        return childrenIdchildren;
    }

    public void setChildrenIdchildren(int childrenIdchildren) {
        this.childrenIdchildren = childrenIdchildren;
    }

    @Column(name = "allergy_idallergy")
    @Id
    public int getAllergyIdallergy() {
        return allergyIdallergy;
    }

    public void setAllergyIdallergy(int allergyIdallergy) {
        this.allergyIdallergy = allergyIdallergy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildrenHasAllergyEntityPK that = (ChildrenHasAllergyEntityPK) o;
        return childrenIdchildren == that.childrenIdchildren &&
                allergyIdallergy == that.allergyIdallergy;
    }

    @Override
    public int hashCode() {

        return Objects.hash(childrenIdchildren, allergyIdallergy);
    }
}
