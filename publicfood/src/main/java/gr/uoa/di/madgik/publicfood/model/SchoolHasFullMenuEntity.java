package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "school_has_full_menu", schema = "food_pilot", catalog = "")
@IdClass(SchoolHasFullMenuEntityPK.class)
public class SchoolHasFullMenuEntity {
    private int schoolId;
    private String schoolDomainName;
    private int fullMenuId;
    private String fullMenuDomain;

    @Id
    @Column(name = "school_id")
    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Id
    @Column(name = "school_Domain_name")
    public String getSchoolDomainName() {
        return schoolDomainName;
    }

    public void setSchoolDomainName(String schoolDomainName) {
        this.schoolDomainName = schoolDomainName;
    }

    @Id
    @Column(name = "full_menu_id")
    public int getFullMenuId() {
        return fullMenuId;
    }

    public void setFullMenuId(int fullMenuId) {
        this.fullMenuId = fullMenuId;
    }

    @Id
    @Column(name = "full_menu_domain")
    public String getFullMenuDomain() {
        return fullMenuDomain;
    }

    public void setFullMenuDomain(String fullMenuDomain) {
        this.fullMenuDomain = fullMenuDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolHasFullMenuEntity that = (SchoolHasFullMenuEntity) o;
        return schoolId == that.schoolId &&
                fullMenuId == that.fullMenuId &&
                Objects.equals(schoolDomainName, that.schoolDomainName) &&
                Objects.equals(fullMenuDomain, that.fullMenuDomain);
    }

    @Override
    public int hashCode() {

        return Objects.hash(schoolId, schoolDomainName, fullMenuId, fullMenuDomain);
    }
}
