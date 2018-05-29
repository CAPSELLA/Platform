package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "post_code", schema = "food_pilot", catalog = "")
@IdClass(PostCodeEntityPK.class)
public class PostCodeEntity {
    private String domainName;
    private int idPostCode;

    @Id
    @Column(name = "Domain_name")
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Id
    @Column(name = "id_post_code")
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
        PostCodeEntity that = (PostCodeEntity) o;
        return idPostCode == that.idPostCode &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(domainName, idPostCode);
    }
}
