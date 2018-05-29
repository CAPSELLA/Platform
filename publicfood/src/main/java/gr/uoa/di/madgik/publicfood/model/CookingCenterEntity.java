package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cooking_center", schema = "food_pilot", catalog = "")
@IdClass(CookingCenterEntityPK.class)
public class CookingCenterEntity {
    private int id;
    private String name;
    private String address;
    private String telephone;
    private String domainName;
    private Double latidude;
    private String longitude;
    private Integer postCodeId;
    private Integer area;


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

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
    @Column(name = "latidude")
    public Double getLatidude() {
        return latidude;
    }

    public void setLatidude(Double latidude) {
        this.latidude = latidude;
    }

    @Basic
    @Column(name = "longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "area")
    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    @Basic
    @Column(name = "post_code_id")
    public Integer getPostCodeId() {
        return postCodeId;
    }

    public void setPostCodeId(Integer postCodeId) {
        this.postCodeId = postCodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookingCenterEntity that = (CookingCenterEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(telephone, that.telephone) &&
                Objects.equals(domainName, that.domainName) &&
                Objects.equals(latidude, that.latidude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(postCodeId, that.postCodeId) &&
                Objects.equals(area, that.area);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, address, telephone, domainName, latidude, longitude, postCodeId, area);
    }
}
