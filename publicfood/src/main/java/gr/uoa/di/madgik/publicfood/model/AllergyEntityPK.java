package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AllergyEntityPK implements Serializable {
    private int idallergy;
    private String domainName;

    @Column(name = "idallergy")
    @Id
    public int getIdallergy() {
        return idallergy;
    }

    public void setIdallergy(int idallergy) {
        this.idallergy = idallergy;
    }

    @Column(name = "Domain_name")
    @Id
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
        AllergyEntityPK that = (AllergyEntityPK) o;
        return idallergy == that.idallergy &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idallergy, domainName);
    }
}
