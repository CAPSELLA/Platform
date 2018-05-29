package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostCodeEntityPK implements Serializable {
    private String domainName;
    private int idPostCode;

    @Column(name = "Domain_name")
    @Id
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Column(name = "id_post_code")
    @Id
    public int getIdPostCode() {
        return idPostCode;
    }

    public void setIdPostCode(int idPostCode) {
        this.idPostCode = idPostCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCodeEntityPK that = (PostCodeEntityPK) o;
        return idPostCode == that.idPostCode &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(domainName, idPostCode);
    }
}
