package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

public class ChildrenEntityPK implements Serializable {
    private Integer idchildren;

    @Column(name = "idchildren")
    @Id
    public Integer getIdchildren() {
        return idchildren;
    }

    public void setIdchildren(Integer idchildren) {
        this.idchildren = idchildren;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildrenEntityPK that = (ChildrenEntityPK) o;
        return idchildren.intValue() == that.idchildren.intValue();
    }

    @Override
    public int hashCode() {

        return Objects.hash(idchildren.intValue());
    }
}
