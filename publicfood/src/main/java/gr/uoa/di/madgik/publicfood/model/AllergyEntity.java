package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "allergy", schema = "food_pilot", catalog = "")
@IdClass(AllergyEntityPK.class)
public class AllergyEntity {
    private int idallergy;
    private String domainName;

    @Id
    @Column(name = "idallergy")
    public int getIdallergy() {
        return idallergy;
    }

    public void setIdallergy(int idallergy) {
        this.idallergy = idallergy;
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
        AllergyEntity that = (AllergyEntity) o;
        return idallergy == that.idallergy &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idallergy, domainName);
    }
}
