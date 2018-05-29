package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "school", schema = "food_pilot", catalog = "")
@IdClass(SchoolEntityPK.class)
public class SchoolEntity {
    private int id;
    private String name;
    private String address;
    private String telephone;
    private Integer cookingCenterId;
    private Double latidude;
    private Double longitude;
    private String domainName;
    private Integer postCodeIdPostCode;

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

    @Basic
    @Column(name = "cooking_center_id")
    public Integer getCookingCenterId() {
        return cookingCenterId;
    }

    public void setCookingCenterId(Integer cookingCenterId) {
        this.cookingCenterId = cookingCenterId;
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
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
    @Column(name = "post_code_id_post_code")
    public Integer getPostCodeIdPostCode() {
        return postCodeIdPostCode;
    }

    public void setPostCodeIdPostCode(Integer postCodeIdPostCode) {
        this.postCodeIdPostCode = postCodeIdPostCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolEntity that = (SchoolEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(telephone, that.telephone) &&
                Objects.equals(cookingCenterId, that.cookingCenterId) &&
                Objects.equals(latidude, that.latidude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(domainName, that.domainName) &&
                Objects.equals(postCodeIdPostCode, that.postCodeIdPostCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, address, telephone, cookingCenterId, latidude, longitude, domainName, postCodeIdPostCode);
    }
}
