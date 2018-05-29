package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authority", schema = "food_pilot", catalog = "")
@IdClass(AuthorityEntityPK.class)
public class AuthorityEntity {
    private long id;
    private String name;
    private String domainName;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        AuthorityEntity that = (AuthorityEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, domainName);
    }
}
