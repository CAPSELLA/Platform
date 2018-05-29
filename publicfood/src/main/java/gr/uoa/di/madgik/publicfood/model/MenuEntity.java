package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu", schema = "food_pilot", catalog = "")
@IdClass(MenuEntityPK.class)
public class MenuEntity {
    private int id;
    private String name;
    private String domainName;
    private int fullMenuId;


    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Basic
    @Column(name = "full_menu_id")
    public int getFullMenuId() {
        return fullMenuId;
    }

    public void setFullMenuId(int fullMenuId) {
        this.fullMenuId = fullMenuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuEntity that = (MenuEntity) o;
        return id == that.id &&
                fullMenuId == that.fullMenuId &&
                Objects.equals(name, that.name) &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, domainName, fullMenuId);
    }
}
