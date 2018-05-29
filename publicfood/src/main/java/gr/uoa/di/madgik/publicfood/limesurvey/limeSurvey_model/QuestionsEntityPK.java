//package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;
//
//import javax.persistence.Column;
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.util.Objects;
//
//public class QuestionsEntityPK implements Serializable {
//    private int qid;
//    private String language;
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
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        QuestionsEntityPK that = (QuestionsEntityPK) o;
//        return qid == that.qid &&
//                Objects.equals(language, that.language);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(qid, language);
//    }
//}
