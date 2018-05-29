package gr.uoa.di.madgik.publicfood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import gr.uoa.di.madgik.publicfood.Utilities.Utils;
import gr.uoa.di.madgik.publicfood.facades.Facade;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import gr.uoa.di.madgik.publicfood.security.repository.AllergyRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "children", schema = "food_pilot", catalog = "")
@IdClass(ChildrenEntityPK.class)
public class ChildrenEntity extends Facade implements Serializable{

    private Integer idchildren;
    private String name;
    private int age;
    private int schoolId;
    private int gender;
    private int height;
    private String domainName;
    private String weight;
    private List<UserEntity> userEntities;
    private List<AllergyEntity> allergyEntities;

    private JSONArray allergens;

    public ChildrenEntity(){
    }

    @Id
    @Column(name = "idchildren")
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    public Integer getIdchildren() {
        return idchildren;
    }

    public void setIdchildren(Integer idchildren) {
        this.idchildren = idchildren;
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
    @Column(name = "weight")
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    @Basic
    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Basic
    @Column(name = "height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Basic
    @Column(name = "gender")
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "school_id")
    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Basic
    @Column(name = "Domain_name")
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }


    @ManyToMany(mappedBy = "childrenEntities")
    @JsonIgnore
    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "children_has_allergy", joinColumns = {@JoinColumn(name = "children_idchildren", referencedColumnName = "idchildren")},
            inverseJoinColumns = {@JoinColumn(name = "allergy_idallergy", referencedColumnName = "idallergy"),
                    @JoinColumn(name = "domain_name", referencedColumnName = "Domain_name")})
    @JsonIgnore
    public List<AllergyEntity> getAllergyEntities() {
        return allergyEntities;
    }

    public void setAllergyEntities(List<AllergyEntity> allergyEntities) {
        this.allergyEntities = allergyEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() ) return false;

        ChildrenEntity that = (ChildrenEntity) o;
        if(that.idchildren == null)
            return false;
        return idchildren.intValue() == that.idchildren.intValue()&&
                schoolId == that.schoolId &&
                Objects.equals(name, that.name) &&
                age == that.age &&
                gender == that.gender &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idchildren.intValue(), name, age, weight,gender, schoolId, domainName);
    }

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Transient
    public JSONArray getAllergens() {
        return allergens;
    }

    public void setAllergens(JSONArray allergens) {
        this.allergens = allergens;
    }

    @Override
    public ChildrenEntity Deserialize(Object payload) throws JSONException {

        JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);
        ChildrenEntity childrenEntity = new ChildrenEntity();
        if(jsonObject.has(ResponseKeys.RegisterUser_Children_Id)){
            childrenEntity.setIdchildren(jsonObject.getInt(ResponseKeys.RegisterUser_Children_Id));
        }
        childrenEntity.gender = (int) jsonObject.get(ResponseKeys.RegisterUser_Gender);
        childrenEntity.age =  Integer.parseInt( (String)jsonObject.get(ResponseKeys.RegisterUser_Age));
        childrenEntity.schoolId = (int) jsonObject.get(ResponseKeys.RegisterUser_School);
        childrenEntity.name = (String) jsonObject.get(ResponseKeys.RegisterUser_Children_Name);
        childrenEntity.weight = (String) jsonObject.get(ResponseKeys.RegisterUser_Weight);
        childrenEntity.domainName = (String) jsonObject.get(ResponseKeys.RegisterUser_Domain);
        childrenEntity.height = jsonObject.getInt(ResponseKeys.RegisterUser_Ηeight);
        childrenEntity.allergens = (JSONArray) jsonObject.get(ResponseKeys.RegisterUser_Allergens);

        return childrenEntity;
    }

    public enum Gender {
        Male(1), Female(2);

        private final int id;

        Gender(int id) {
            this.id = id;
        }

        public static Gender valueOf(int x) {
            switch (x) {
                case 1:
                    return Male;
                case 2:
                    return Female;

            }
            throw new RuntimeException("Invalid enum value");
        }

        public String toString() {
            switch (this) {
                case Male:
                    return "Male";
                case Female:
                    return "Female";

            }
            return "";
        }



        public int getValue() {
            return id;
        }
    }
}
