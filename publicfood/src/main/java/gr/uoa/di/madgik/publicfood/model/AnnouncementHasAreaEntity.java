package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "announcement_has_area", schema = "food_pilot", catalog = "")
public class AnnouncementHasAreaEntity {
    private int id;
    private int announcementId;
    private int areaId;
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
    @Column(name = "domain")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Basic
    @Column(name = "announcement_id")
    public int getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(int announcementId) {
        this.announcementId = announcementId;
    }

    @Basic
    @Column(name = "area_id")
    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnouncementHasAreaEntity that = (AnnouncementHasAreaEntity) o;
        return id == that.id &&
                announcementId == that.announcementId &&
                Objects.equals(domain, that.domain) &&
                areaId == that.areaId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, domain, announcementId, areaId);
    }
}
