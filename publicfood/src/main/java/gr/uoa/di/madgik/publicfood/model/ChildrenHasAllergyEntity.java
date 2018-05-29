package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "children_has_allergy", schema = "food_pilot", catalog = "")
@IdClass(ChildrenHasAllergyEntityPK.class)
public class ChildrenHasAllergyEntity {
    private int childrenIdchildren;
    private String domainName;
    private int allergyIdallergy;

    @Id
    @Column(name = "children_idchildren")
    public int getChildrenIdchildren() {
        return childrenIdchildren;
    }

    public void setChildrenIdchildren(int childrenIdchildren) {
        this.childrenIdchildren = childrenIdchildren;
    }

    @Basic
    @Column(name = "domain_name")
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Id
    @Column(name = "allergy_idallergy")
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
        ChildrenHasAllergyEntity that = (ChildrenHasAllergyEntity) o;
        return childrenIdchildren == that.childrenIdchildren &&
                allergyIdallergy == that.allergyIdallergy &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(childrenIdchildren, domainName, allergyIdallergy);
    }
}
