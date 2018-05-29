package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParticipantEntityPK implements Serializable {
    private Integer tid;

    @Id
    @Column(name = "tid", unique = true, nullable = false)
    public Integer getTid() {
        return this.tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantEntityPK that = (ParticipantEntityPK) o;
        return tid.intValue() == that.tid.intValue();
    }

    @Override
    public int hashCode() {

        return Objects.hash(tid.intValue());
    }
}
