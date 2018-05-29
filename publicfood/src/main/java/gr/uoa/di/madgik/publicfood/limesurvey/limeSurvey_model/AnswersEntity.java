//package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;
//
//import javax.persistence.*;
//import java.util.Objects;
//
//@Entity
//@Table(name = "answers", schema = "LimeSurvey", catalog = "")
//@IdClass(AnswersEntityPK.class)
//public class AnswersEntity {
//    private int qid;
//    private String code;
//    private String answer;
//    private int sortorder;
//    private int assessmentValue;
//    private String language;
//    private int scaleId;
//
//    @Id
//    @Column(name = "qid")
//    public int getQid() {
//        return qid;
//    }
//
//    public void setQid(int qid) {
//        this.qid = qid;
//    }
//
//    @Id
//    @Column(name = "code")
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    @Basic
//    @Column(name = "answer")
//    public String getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//    }
//
//    @Basic
//    @Column(name = "sortorder")
//    public int getSortorder() {
//        return sortorder;
//    }
//
//    public void setSortorder(int sortorder) {
//        this.sortorder = sortorder;
//    }
//
//    @Basic
//    @Column(name = "assessment_value")
//    public int getAssessmentValue() {
//        return assessmentValue;
//    }
//
//    public void setAssessmentValue(int assessmentValue) {
//        this.assessmentValue = assessmentValue;
//    }
//
//    @Id
//    @Column(name = "language")
//    public String getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(String language) {
//        this.language = language;
//    }
//
//    @Id
//    @Column(name = "scale_id")
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
//        AnswersEntity that = (AnswersEntity) o;
//        return qid == that.qid &&
//                sortorder == that.sortorder &&
//                assessmentValue == that.assessmentValue &&
//                scaleId == that.scaleId &&
//                Objects.equals(code, that.code) &&
//                Objects.equals(answer, that.answer) &&
//                Objects.equals(language, that.language);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(qid, code, answer, sortorder, assessmentValue, language, scaleId);
//    }
//}
