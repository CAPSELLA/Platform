package gr.uoa.di.madgik.publicfood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "food_pilot", catalog = "")
@IdClass(UserEntityPK.class)
public class UserEntity {
    private long id;
    private String displayname;
    private String email;
    private String picture;
    private String provider;
    private int providerid;
    private String token;
    private int status;
    private String domainName;
    private String providerUuid;
    private Integer postCode;
    private Integer age;
    private Integer education;
    private Integer profession;
    private String limeSurveyRegistrationToken;
    private String limeSurveyWeeklyToken;

    private List<ChildrenEntity> childrenEntities;
    private boolean isMale = false;

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "displayname")
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Basic
    @Column(name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Basic
    @Column(name = "providerid")
    public int getProviderid() {
        return providerid;
    }

    public void setProviderid(int providerid) {
        this.providerid = providerid;
    }

    @Basic
    @Column(name = "limeSurveyWeeklyToken")
    public String getLimeSurveyWeeklyToken() {
        return limeSurveyWeeklyToken;
    }

    public void setLimeSurveyWeeklyToken(String limeSurveyWeeklyToken) {
        this.limeSurveyWeeklyToken = limeSurveyWeeklyToken;
    }

    @Basic
    @Column(name = "limeSurveyRegistrationToken")
    public String getLimeSurveyRegistrationToken() {
        return limeSurveyRegistrationToken;
    }

    public void setLimeSurveyRegistrationToken(String limeSurveyRegistrationToken) {
        this.limeSurveyRegistrationToken = limeSurveyRegistrationToken;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
    @Column(name = "providerUUID")
    public String getProviderUuid() {
        return providerUuid;
    }

    public void setProviderUuid(String providerUuid) {
        this.providerUuid = providerUuid;
    }

    @Basic
    @Column(name = "post_code")
    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "education")
    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    @Basic
    @Column(name = "profession")
    public Integer getProfession() {
        return profession;
    }

    public void setProfession(Integer profession) {
        this.profession = profession;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_has_children", joinColumns ={@JoinColumn(name = "user_id", referencedColumnName = "id"), @JoinColumn(name = "domain_name", referencedColumnName = "Domain_name")} ,
            inverseJoinColumns = {@JoinColumn(name = "children_idchildren", referencedColumnName = "idchildren") })
    @JsonIgnore
    public List<ChildrenEntity> getChildrenEntities() {
        return childrenEntities;
    }

    public void setChildrenEntities(List<ChildrenEntity> childrenEntities) {
        this.childrenEntities = childrenEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                providerid == that.providerid &&
                status == that.status &&
                Objects.equals(displayname, that.displayname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(provider, that.provider) &&
                Objects.equals(providerid, that.providerid) &&
                Objects.equals(token, that.token) &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, displayname, email, picture, provider, providerid, token, status, domainName);
    }

    public enum Status {
        UNAUTHORIZED(0), SIGNED_IN(1), REGISTERED(2), FULLY_REGISTERED(3), EXISTED(4);

        private final int id;

        Status(int id) {
            this.id = id;
        }

        public static Status valueOf(int x) {
            switch (x) {
                case 0:
                    return UNAUTHORIZED;
                case 1:
                    return SIGNED_IN;
                case 2:
                    return REGISTERED;
                case 3:
                    return FULLY_REGISTERED;
                case 4:
                    return EXISTED;

            }
            return null;
        }

        public int getValue() {
            return id;
        }
    }


    public JSONObject Serialize(){

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id);
        jsonObject.put("displayName", displayname);
        jsonObject.put("email", email);
        jsonObject.put("picture", picture);
        jsonObject.put("provider", provider);
        jsonObject.put("providerUUID", providerUuid);
        jsonObject.put("providerid", providerid);
        jsonObject.put("token", token);
        jsonObject.put("limeSurveyRegistrationToken", limeSurveyRegistrationToken);
        jsonObject.put("limeSurveyWeeklyToken", limeSurveyWeeklyToken);
        jsonObject.put("status", status);
        jsonObject.put("domainName", domainName);

        return jsonObject;
    }

}
