package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "announcement", schema = "food_pilot", catalog = "")
public class AnnouncementEntity {
    private int id;
    private String domain;
    private Timestamp createdAt;
    private int type;
    private String link;
    private Timestamp validFrom;
    private Timestamp validTo;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "domain")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Basic
    @Column(name = "createdAt")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnouncementEntity that = (AnnouncementEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(link, that.link) &&
                Objects.equals(validFrom, that.validFrom) &&
                Objects.equals(validTo, that.validTo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, domain, createdAt, type, link, validFrom, validTo);
    }
}
