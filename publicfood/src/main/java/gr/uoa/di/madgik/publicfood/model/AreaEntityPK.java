package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AreaEntityPK implements Serializable {
    private int idarea;
    private String domainName;

    @Column(name = "idarea")
    @Id
    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
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
        AreaEntityPK that = (AreaEntityPK) o;
        return idarea == that.idarea &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idarea, domainName);
    }
}
