package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "area", schema = "food_pilot", catalog = "")
@IdClass(AreaEntityPK.class)
public class AreaEntity {
    private int idarea;
    private String domainName;
    private int startingWeek;

    @Id
    @Column(name = "idarea")
    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    @Basic
    @Column(name = "starting_week")
    public int getStartingWeek() {
        return startingWeek;
    }

    public void setStartingWeek(int startingWeek) {
        this.startingWeek = startingWeek;
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
        AreaEntity that = (AreaEntity) o;
        return idarea == that.idarea &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idarea, domainName);
    }
}
