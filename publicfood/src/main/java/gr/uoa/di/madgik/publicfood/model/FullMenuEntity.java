package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "full_menu", schema = "food_pilot", catalog = "")
@IdClass(FullMenuEntityPK.class)
public class FullMenuEntity {
    private int id;
    private Timestamp validFrom;
    private Timestamp validTo;
    private Integer active;
    private String domain;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "valid_from")
    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    @Basic
    @Column(name = "valid_to")
    public Timestamp getValidTo() {
        return validTo;
    }

    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

    @Basic
    @Column(name = "active")
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Id
    @Column(name = "domain")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullMenuEntity that = (FullMenuEntity) o;
        return id == that.id &&
                Objects.equals(validFrom, that.validFrom) &&
                Objects.equals(validTo, that.validTo) &&
                Objects.equals(active, that.active) &&
                Objects.equals(domain, that.domain);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, validFrom, validTo, active, domain);
    }
}
