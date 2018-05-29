package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_has_children", schema = "food_pilot", catalog = "")
@IdClass(UserHasChildrenEntityPK.class)
public class UserHasChildrenEntity {
    private long userId;
    private String domainName;
    private int childrenIdchildren;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "domain_name")
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Id
    @Column(name = "children_idchildren")
    public int getChildrenIdchildren() {
        return childrenIdchildren;
    }

    public void setChildrenIdchildren(int childrenIdchildren) {
        this.childrenIdchildren = childrenIdchildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasChildrenEntity that = (UserHasChildrenEntity) o;
        return userId == that.userId &&
                childrenIdchildren == that.childrenIdchildren &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, domainName, childrenIdchildren);
    }
}
