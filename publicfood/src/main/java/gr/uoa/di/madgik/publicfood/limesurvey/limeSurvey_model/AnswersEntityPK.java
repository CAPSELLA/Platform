//package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;
//
//import javax.persistence.Column;
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.util.Objects;
//
//public class AnswersEntityPK implements Serializable {
//    private int qid;
//    private String code;
//    private String language;
//    private int scaleId;
//
//    @Column(name = "qid")
//    @Id
//    public int getQid() {
//        return qid;
//    }
//
//    public void setQid(int qid) {
//        this.qid = qid;
//    }
//
//    @Column(name = "code")
//    @Id
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    @Column(name = "language")
//    @Id
//    public String getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(String language) {
//        this.language = language;
//    }
//
//    @Column(name = "scale_id")
//    @Id
//    public int getScaleId() {
//        return scaleId;
//    }
//
//    public void setScaleId(int scaleId) {
//        this.scaleId = scaleId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AnswersEntityPK that = (AnswersEntityPK) o;
//        return qid == that.qid &&
//                scaleId == that.scaleId &&
//                Objects.equals(code, that.code) &&
//                Objects.equals(language, that.language);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(qid, code, language, scaleId);
//    }
//}
