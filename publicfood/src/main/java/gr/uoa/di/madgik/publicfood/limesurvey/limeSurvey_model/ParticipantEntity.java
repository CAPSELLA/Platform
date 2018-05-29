package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "tokens_975189", schema = "LimeSurvey", catalog = "")
@IdClass(ParticipantEntityPK.class)
public class ParticipantEntity implements java.io.Serializable {

    private Integer tid;
    private String participantId;
    private String firstname;
    private String lastname;
    private String email;
    private String emailstatus;
    private String token;
    private String language;
    private String blacklisted;
    private String sent;
    private String remindersent;
    private Integer remindercount;
    private String completed;
    private Integer usesleft;
    private Date validfrom;
    private Date validuntil;
    private Integer mpid;

    public ParticipantEntity() {
    }

    public ParticipantEntity(String participantId, String firstname, String lastname, String email, String emailstatus,
                             String token, String language, String blacklisted, String sent, String remindersent, Integer remindercount,
                             String completed, Integer usesleft, Date validfrom, Date validuntil, Integer mpid) {
        this.participantId = participantId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.emailstatus = emailstatus;
        this.token = token;
        this.language = language;
        this.blacklisted = blacklisted;
        this.sent = sent;
        this.remindersent = remindersent;
        this.remindercount = remindercount;
        this.completed = completed;
        this.usesleft = usesleft;
        this.validfrom = validfrom;
        this.validuntil = validuntil;
        this.mpid = mpid;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tid", unique = true, nullable = false)
    public Integer getTid() {
        return this.tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    @Basic
    @Column(name = "participant_id", length = 50)
    public String getParticipantId() {
        return this.participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    @Basic
    @Column(name = "firstname", length = 150)
    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "lastname", length = 150)
    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "email", length = 65535)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "emailstatus", length = 65535)
    public String getEmailstatus() {
        return this.emailstatus;
    }

    public void setEmailstatus(String emailstatus) {
        this.emailstatus = emailstatus;
    }

    @Basic
    @Column(name = "token", length = 35)
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "language", length = 25)
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "blacklisted", length = 17)
    public String getBlacklisted() {
        return this.blacklisted;
    }

    public void setBlacklisted(String blacklisted) {
        this.blacklisted = blacklisted;
    }

    @Basic
    @Column(name = "sent", length = 17)
    public String getSent() {
        return this.sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    @Basic
    @Column(name = "remindersent", length = 17)
    public String getRemindersent() {
        return this.remindersent;
    }

    public void setRemindersent(String remindersent) {
        this.remindersent = remindersent;
    }

    @Basic
    @Column(name = "remindercount")
    public Integer getRemindercount() {
        return this.remindercount;
    }

    public void setRemindercount(Integer remindercount) {
        this.remindercount = remindercount;
    }

    @Basic
    @Column(name = "completed", length = 17)
    public String getCompleted() {
        return this.completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    @Basic
    @Column(name = "usesleft")
    public Integer getUsesleft() {
        return this.usesleft;
    }

    public void setUsesleft(Integer usesleft) {
        this.usesleft = usesleft;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validfrom", length = 19)
    public Date getValidfrom() {
        return this.validfrom;
    }

    public void setValidfrom(Date validfrom) {
        this.validfrom = validfrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validuntil", length = 19)
    public Date getValiduntil() {
        return this.validuntil;
    }

    public void setValiduntil(Date validuntil) {
        this.validuntil = validuntil;
    }

    @Column(name = "mpid")
    public Integer getMpid() {
        return this.mpid;
    }

    public void setMpid(Integer mpid) {
        this.mpid = mpid;
    }

}
