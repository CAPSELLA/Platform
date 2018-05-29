package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class SchoolHasFullMenuEntityPK implements Serializable {
    private int schoolId;
    private String schoolDomainName;
    private int fullMenuId;
    private String fullMenuDomain;

    @Column(name = "school_id")
    @Id
    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Column(name = "school_Domain_name")
    @Id
    public String getSchoolDomainName() {
        return schoolDomainName;
    }

    public void setSchoolDomainName(String schoolDomainName) {
        this.schoolDomainName = schoolDomainName;
    }

    @Column(name = "full_menu_id")
    @Id
    public int getFullMenuId() {
        return fullMenuId;
    }

    public void setFullMenuId(int fullMenuId) {
        this.fullMenuId = fullMenuId;
    }

    @Column(name = "full_menu_domain")
    @Id
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
        SchoolHasFullMenuEntityPK that = (SchoolHasFullMenuEntityPK) o;
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
