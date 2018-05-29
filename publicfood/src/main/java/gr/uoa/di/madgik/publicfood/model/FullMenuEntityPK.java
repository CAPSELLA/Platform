package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class FullMenuEntityPK implements Serializable {
    private int id;
    private String domain;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "domain")
    @Id
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullMenuEntityPK that = (FullMenuEntityPK) o;
        return id == that.id &&
                Objects.equals(domain, that.domain);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, domain);
    }
}
