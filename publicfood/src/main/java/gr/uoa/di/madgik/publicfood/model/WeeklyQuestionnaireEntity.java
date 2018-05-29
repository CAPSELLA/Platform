package gr.uoa.di.madgik.publicfood.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "weekly_questionnaire", schema = "food_pilot", catalog = "")
public class WeeklyQuestionnaireEntity {
    private int id;
    private String userId;
    private String domain;
    private byte completed;
    private Timestamp deadline;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "domain")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Basic
    @Column(name = "completed")
    public byte getCompleted() {
        return completed;
    }

    public void setCompleted(byte completed) {
        this.completed = completed;
    }

    @Basic
    @Column(name = "deadline")
    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyQuestionnaireEntity that = (WeeklyQuestionnaireEntity) o;
        return id == that.id &&
                completed == that.completed &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(deadline, that.deadline);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, domain, completed, deadline);
    }


    public enum Status {
        RESPOND(0), MISSED(1), COMPLETED(2);

        private final int id;

        Status(int id) {
            this.id = id;
        }

        public static Status valueOf(int x) {
            switch (x) {
                case 0:
                    return RESPOND;
                case 1:
                    return MISSED;
                case 2:
                    return COMPLETED;

            }
            return null;
        }

        public int getValue() {
            return id;
        }
    }




}
