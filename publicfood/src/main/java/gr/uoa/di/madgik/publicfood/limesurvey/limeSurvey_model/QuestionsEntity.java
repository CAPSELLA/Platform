//package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;
//
//import javax.persistence.*;
//import java.util.Objects;
//
//@Entity
//@Table(name = "questions", schema = "LimeSurvey", catalog = "")
//@IdClass(QuestionsEntityPK.class)
//public class QuestionsEntity {
//    private int qid;
//    private int parentQid;
//    private int sid;
//    private int gid;
//    private String type;
//    private String title;
//    private String question;
//    private String preg;
//    private String help;
//    private String other;
//    private String mandatory;
//    private int questionOrder;
//    private String language;
//    private int scaleId;
//    private int sameDefault;
//    private String relevance;
//    private String modulename;
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
//    @Basic
//    @Column(name = "parent_qid")
//    public int getParentQid() {
//        return parentQid;
//    }
//
//    public void setParentQid(int parentQid) {
//        this.parentQid = parentQid;
//    }
//
//    @Basic
//    @Column(name = "sid")
//    public int getSid() {
//        return sid;
//    }
//
//    public void setSid(int sid) {
//        this.sid = sid;
//    }
//
//    @Basic
//    @Column(name = "gid")
//    public int getGid() {
//        return gid;
//    }
//
//    public void setGid(int gid) {
//        this.gid = gid;
//    }
//
//    @Basic
//    @Column(name = "type")
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    @Basic
//    @Column(name = "title")
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    @Basic
//    @Column(name = "question")
//    public String getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    @Basic
//    @Column(name = "preg")
//    public String getPreg() {
//        return preg;
//    }
//
//    public void setPreg(String preg) {
//        this.preg = preg;
//    }
//
//    @Basic
//    @Column(name = "help")
//    public String getHelp() {
//        return help;
//    }
//
//    public void setHelp(String help) {
//        this.help = help;
//    }
//
//    @Basic
//    @Column(name = "other")
//    public String getOther() {
//        return other;
//    }
//
//    public void setOther(String other) {
//        this.other = other;
//    }
//
//    @Basic
//    @Column(name = "mandatory")
//    public String getMandatory() {
//        return mandatory;
//    }
//
//    public void setMandatory(String mandatory) {
//        this.mandatory = mandatory;
//    }
//
//    @Basic
//    @Column(name = "question_order")
//    public int getQuestionOrder() {
//        return questionOrder;
//    }
//
//    public void setQuestionOrder(int questionOrder) {
//        this.questionOrder = questionOrder;
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
//    @Basic
//    @Column(name = "scale_id")
//    public int getScaleId() {
//        return scaleId;
//    }
//
//    public void setScaleId(int scaleId) {
//        this.scaleId = scaleId;
//    }
//
//    @Basic
//    @Column(name = "same_default")
//    public int getSameDefault() {
//        return sameDefault;
//    }
//
//    public void setSameDefault(int sameDefault) {
//        this.sameDefault = sameDefault;
//    }
//
//    @Basic
//    @Column(name = "relevance")
//    public String getRelevance() {
//        return relevance;
//    }
//
//    public void setRelevance(String relevance) {
//        this.relevance = relevance;
//    }
//
//    @Basic
//    @Column(name = "modulename")
//    public String getModulename() {
//        return modulename;
//    }
//
//    public void setModulename(String modulename) {
//        this.modulename = modulename;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        QuestionsEntity that = (QuestionsEntity) o;
//        return qid == that.qid &&
//                parentQid == that.parentQid &&
//                sid == that.sid &&
//                gid == that.gid &&
//                questionOrder == that.questionOrder &&
//                scaleId == that.scaleId &&
//                sameDefault == that.sameDefault &&
//                Objects.equals(type, that.type) &&
//                Objects.equals(title, that.title) &&
//                Objects.equals(question, that.question) &&
//                Objects.equals(preg, that.preg) &&
//                Objects.equals(help, that.help) &&
//                Objects.equals(other, that.other) &&
//                Objects.equals(mandatory, that.mandatory) &&
//                Objects.equals(language, that.language) &&
//                Objects.equals(relevance, that.relevance) &&
//                Objects.equals(modulename, that.modulename);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(qid, parentQid, sid, gid, type, title, question, preg, help, other, mandatory, questionOrder, language, scaleId, sameDefault, relevance, modulename);
//    }
//}
