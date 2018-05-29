package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserHasChildrenEntityPK implements Serializable {
    private long userId;
    private String domainName;
    private int childrenIdchildren;

    @Column(name = "user_id")
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "domain_name")
    @Id
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @Column(name = "children_idchildren")
    @Id
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
        UserHasChildrenEntityPK that = (UserHasChildrenEntityPK) o;
        return userId == that.userId &&
                childrenIdchildren == that.childrenIdchildren &&
                Objects.equals(domainName, that.domainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, domainName, childrenIdchildren);
    }
}
