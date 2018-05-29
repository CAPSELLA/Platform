//package gr.uoa.di.madgik.publicfood.limesurvey.limeSurvey_model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import gr.uoa.di.madgik.publicfood.model.UserEntity;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.Objects;
//
//@Entity
//@Table(name = "survey_975189", schema = "LimeSurvey", catalog = "")
//public class SurveyEntity {
//    private int id;
//    private String token;
//    private Timestamp submitdate;
//    private Integer lastpage;
//    private String startlanguage;
//    private String seed;
//    private Timestamp startdate;
//    private Timestamp datestamp;
//    private String ipaddr;
//    private String question1;
//    private String question3;
//    private String question4;
//    private String question5;
//    private String question6;
//    private String question7;
//    private String question8;
//    private String question2;
//    private String question9;
//    private String question22;
//    private String question10;
//    private String question11;
//    private String question12;
//    private String question13;
//    private String question14;
//    private String question15;
//    private String question16;
//    private String question17;
//    private String question18;
//    private String question19;
//    private String question20;
//    private String question21;
//    private String question75;
//    private String question76;
//    private String question77;
//    private String question78;
//    private String question79;
//    private String question30;
//    private BigDecimal question23;
//    private String question24;
//    private String question25;
//    private String question26;
//    private String question27;
//    private String question28;
//    private String question29;
//    private String question31;
//    private String question32;
//    private String question33;
//    private String question34;
//    private String question35;
//    private String question36;
//    private String question37;
//    private String question38;
//    private String question39;
//    private String question40;
//    private String question41;
//    private String question42;
//    private String question43;
//    private String question44;
//    private String question45;
//    private String question46;
//    private String question47;
//    private String question48;
//    private String question49;
//    private String question50;
//    private String question51;
//    private String question52;
//    private String question53;
//    private String question54;
//    private String question55;
//    private String question56;
//    private String question83;
//    private String question87;
//    private String question80;
//    private String question84;
//    private String question81;
//    private String question85;
//    private String question82;
//    private String question86;
//    private String question64;
//    private String question65;
//    private String question66;
//    private String question57;
//    private String question58;
//    private String question59;
//    private String question60;
//    private String question61;
//    private String question62;
//    private String question63;
//    private String question67;
//    private String question73;
//    private String question74;
//    private String question68;
//    private String question70;
//    private String question69;
//    private String question71;
//    private String question72;
//
//    @JsonIgnore
//    private boolean isMale = false;
//
//    public boolean isMale() {
//        return isMale;
//    }
//
//    public void setMale(boolean male) {
//        isMale = male;
//    }
//
//    @Id
//    @Column(name = "id")
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    @Basic
//    @Column(name = "token")
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    @Basic
//    @Column(name = "submitdate")
//    public Timestamp getSubmitdate() {
//        return submitdate;
//    }
//
//    public void setSubmitdate(Timestamp submitdate) {
//        this.submitdate = submitdate;
//    }
//
//    @Basic
//    @Column(name = "lastpage")
//    public Integer getLastpage() {
//        return lastpage;
//    }
//
//    public void setLastpage(Integer lastpage) {
//        this.lastpage = lastpage;
//    }
//
//    @Basic
//    @Column(name = "startlanguage")
//    public String getStartlanguage() {
//        return startlanguage;
//    }
//
//    public void setStartlanguage(String startlanguage) {
//        this.startlanguage = startlanguage;
//    }
//
//    @Basic
//    @Column(name = "seed")
//    public String getSeed() {
//        return seed;
//    }
//
//    public void setSeed(String seed) {
//        this.seed = seed;
//    }
//
//    @Basic
//    @Column(name = "startdate")
//    public Timestamp getStartdate() {
//        return startdate;
//    }
//
//    public void setStartdate(Timestamp startdate) {
//        this.startdate = startdate;
//    }
//
//    @Basic
//    @Column(name = "datestamp")
//    public Timestamp getDatestamp() {
//        return datestamp;
//    }
//
//    public void setDatestamp(Timestamp datestamp) {
//        this.datestamp = datestamp;
//    }
//
//    @Basic
//    @Column(name = "ipaddr")
//    public String getIpaddr() {
//        return ipaddr;
//    }
//
//    public void setIpaddr(String ipaddr) {
//        this.ipaddr = ipaddr;
//    }
//
//    @Basic
//    @Column(name = "975189X20X286")
//    public String getQuestion1() {
//        return question1;
//    }
//
//    public void setQuestion1(String question1) {
//        this.question1 = question1;
//    }
//
//    @Basic
//    @Column(name = "975189X20X288SQ001")
//    public String getQuestion3() {
//        return question3;
//    }
//
//    public void setQuestion3(String question3) {
//        this.question3 = question3;
//    }
//
//    @Basic
//    @Column(name = "975189X20X288SQ002")
//    public String getQuestion4() {
//        return question4;
//    }
//
//    public void setQuestion4(String question4) {
//        this.question4 = question4;
//    }
//
//    @Basic
//    @Column(name = "975189X20X288SQ003")
//    public String getQuestion5() {
//        return question5;
//    }
//
//    public void setQuestion5(String question5) {
//        this.question5 = question5;
//    }
//
//    @Basic
//    @Column(name = "975189X20X289SQ001")
//    public String getQuestion6() {
//        return question6;
//    }
//
//    public void setQuestion6(String question6) {
//        this.question6 = question6;
//    }
//
//    @Basic
//    @Column(name = "975189X20X289SQ002")
//    public String getQuestion7() {
//        return question7;
//    }
//
//    public void setQuestion7(String question7) {
//        this.question7 = question7;
//    }
//
//    @Basic
//    @Column(name = "975189X20X289SQ003")
//    public String getQuestion8() {
//        return question8;
//    }
//
//    public void setQuestion8(String question8) {
//        this.question8 = question8;
//    }
//
//    @Basic
//    @Column(name = "975189X20X287")
//    public String getQuestion2() {
//        return question2;
//    }
//
//    public void setQuestion2(String question2) {
//        this.question2 = question2;
//    }
//
//    @Basic
//    @Column(name = "975189X20X308")
//    public String getQuestion9() {
//        return question9;
//    }
//
//    public void setQuestion9(String question9) {
//        this.question9 = question9;
//    }
//
//    @Basic
//    @Column(name = "975189X21X367")
//    public String getQuestion22() {
//        return question22;
//    }
//
//    public void setQuestion22(String question22) {
//        this.question22 = question22;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ001")
//    public String getQuestion10() {
//        return question10;
//    }
//
//    public void setQuestion10(String question10) {
//        this.question10 = question10;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ002")
//    public String getQuestion11() {
//        return question11;
//    }
//
//    public void setQuestion11(String question11) {
//        this.question11 = question11;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ003")
//    public String getQuestion12() {
//        return question12;
//    }
//
//    public void setQuestion12(String question12) {
//        this.question12 = question12;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ004")
//    public String getQuestion13() {
//        return question13;
//    }
//
//    public void setQuestion13(String question13) {
//        this.question13 = question13;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ005")
//    public String getQuestion14() {
//        return question14;
//    }
//
//    public void setQuestion14(String question14) {
//        this.question14 = question14;
//    }
//
//    @Basic
//    @Column(name = "975189X21X298SQ006")
//    public String getQuestion15() {
//        return question15;
//    }
//
//    public void setQuestion15(String question15) {
//        this.question15 = question15;
//    }
//
//    @Basic
//    @Column(name = "975189X21X299SQ001")
//    public String getQuestion16() {
//        return question16;
//    }
//
//    public void setQuestion16(String question16) {
//        this.question16 = question16;
//    }
//
//    @Basic
//    @Column(name = "975189X21X299SQ002")
//    public String getQuestion17() {
//        return question17;
//    }
//
//    public void setQuestion17(String question17) {
//        this.question17 = question17;
//    }
//
//    @Basic
//    @Column(name = "975189X21X299SQ003")
//    public String getQuestion18() {
//        return question18;
//    }
//
//    public void setQuestion18(String question18) {
//        this.question18 = question18;
//    }
//
//    @Basic
//    @Column(name = "975189X21X299SQ004")
//    public String getQuestion19() {
//        return question19;
//    }
//
//    public void setQuestion19(String question19) {
//        this.question19 = question19;
//    }
//
//    @Basic
//    @Column(name = "975189X21X299SQ005")
//    public String getQuestion20() {
//        return question20;
//    }
//
//    public void setQuestion20(String question20) {
//        this.question20 = question20;
//    }
//
//    @Basic
//    @Column(name = "975189X21X306")
//    public String getQuestion21() {
//        return question21;
//    }
//
//    public void setQuestion21(String question21) {
//        this.question21 = question21;
//    }
//
//    @Basic
//    @Column(name = "975189X25X294SQ001")
//    public String getQuestion75() {
//        return question75;
//    }
//
//    public void setQuestion75(String question75) {
//        this.question75 = question75;
//    }
//
//    @Basic
//    @Column(name = "975189X25X294SQ002")
//    public String getQuestion76() {
//        return question76;
//    }
//
//    public void setQuestion76(String question76) {
//        this.question76 = question76;
//    }
//
//    @Basic
//    @Column(name = "975189X25X294SQ003")
//    public String getQuestion77() {
//        return question77;
//    }
//
//    public void setQuestion77(String question77) {
//        this.question77 = question77;
//    }
//
//    @Basic
//    @Column(name = "975189X25X294SQ004")
//    public String getQuestion78() {
//        return question78;
//    }
//
//    public void setQuestion78(String question78) {
//        this.question78 = question78;
//    }
//
//    @Basic
//    @Column(name = "975189X25X368")
//    public String getQuestion79() {
//        return question79;
//    }
//
//    public void setQuestion79(String question79) {
//        this.question79 = question79;
//    }
//
//    @Basic
//    @Column(name = "975189X22X304")
//    public String getQuestion30() {
//        return question30;
//    }
//
//    public void setQuestion30(String question30) {
//        this.question30 = question30;
//    }
//
//    @Basic
//    @Column(name = "975189X22X296")
//    public BigDecimal getQuestion23() {
//        return question23;
//    }
//
//    public void setQuestion23(BigDecimal question23) {
//        this.question23 = question23;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ001")
//    public String getQuestion24() {
//        return question24;
//    }
//
//    public void setQuestion24(String question24) {
//        this.question24 = question24;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ002")
//    public String getQuestion25() {
//        return question25;
//    }
//
//    public void setQuestion25(String question25) {
//        this.question25 = question25;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ003")
//    public String getQuestion26() {
//        return question26;
//    }
//
//    public void setQuestion26(String question26) {
//        this.question26 = question26;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ004")
//    public String getQuestion27() {
//        return question27;
//    }
//
//    public void setQuestion27(String question27) {
//        this.question27 = question27;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ005")
//    public String getQuestion28() {
//        return question28;
//    }
//
//    public void setQuestion28(String question28) {
//        this.question28 = question28;
//    }
//
//    @Basic
//    @Column(name = "975189X22X297SQ006")
//    public String getQuestion29() {
//        return question29;
//    }
//
//    public void setQuestion29(String question29) {
//        this.question29 = question29;
//    }
//
//    @Basic
//    @Column(name = "975189X22X309")
//    public String getQuestion31() {
//        return question31;
//    }
//
//    public void setQuestion31(String question31) {
//        this.question31 = question31;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ001")
//    public String getQuestion32() {
//        return question32;
//    }
//
//    public void setQuestion32(String question32) {
//        this.question32 = question32;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ002")
//    public String getQuestion33() {
//        return question33;
//    }
//
//    public void setQuestion33(String question33) {
//        this.question33 = question33;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ003")
//    public String getQuestion34() {
//        return question34;
//    }
//
//    public void setQuestion34(String question34) {
//        this.question34 = question34;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ004")
//    public String getQuestion35() {
//        return question35;
//    }
//
//    public void setQuestion35(String question35) {
//        this.question35 = question35;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ005")
//    public String getQuestion36() {
//        return question36;
//    }
//
//    public void setQuestion36(String question36) {
//        this.question36 = question36;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ006")
//    public String getQuestion37() {
//        return question37;
//    }
//
//    public void setQuestion37(String question37) {
//        this.question37 = question37;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ007")
//    public String getQuestion38() {
//        return question38;
//    }
//
//    public void setQuestion38(String question38) {
//        this.question38 = question38;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ009")
//    public String getQuestion39() {
//        return question39;
//    }
//
//    public void setQuestion39(String question39) {
//        this.question39 = question39;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ010")
//    public String getQuestion40() {
//        return question40;
//    }
//
//    public void setQuestion40(String question40) {
//        this.question40 = question40;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ011")
//    public String getQuestion41() {
//        return question41;
//    }
//
//    public void setQuestion41(String question41) {
//        this.question41 = question41;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ012")
//    public String getQuestion42() {
//        return question42;
//    }
//
//    public void setQuestion42(String question42) {
//        this.question42 = question42;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ013")
//    public String getQuestion43() {
//        return question43;
//    }
//
//    public void setQuestion43(String question43) {
//        this.question43 = question43;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ014")
//    public String getQuestion44() {
//        return question44;
//    }
//
//    public void setQuestion44(String question44) {
//        this.question44 = question44;
//    }
//
//    @Basic
//    @Column(name = "975189X22X311SQ015")
//    public String getQuestion45() {
//        return question45;
//    }
//
//    public void setQuestion45(String question45) {
//        this.question45 = question45;
//    }
//
//    @Basic
//    @Column(name = "975189X22X312")
//    public String getQuestion46() {
//        return question46;
//    }
//
//    public void setQuestion46(String question46) {
//        this.question46 = question46;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ001")
//    public String getQuestion47() {
//        return question47;
//    }
//
//    public void setQuestion47(String question47) {
//        this.question47 = question47;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ002")
//    public String getQuestion48() {
//        return question48;
//    }
//
//    public void setQuestion48(String question48) {
//        this.question48 = question48;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ003")
//    public String getQuestion49() {
//        return question49;
//    }
//
//    public void setQuestion49(String question49) {
//        this.question49 = question49;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ004")
//    public String getQuestion50() {
//        return question50;
//    }
//
//    public void setQuestion50(String question50) {
//        this.question50 = question50;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ005")
//    public String getQuestion51() {
//        return question51;
//    }
//
//    public void setQuestion51(String question51) {
//        this.question51 = question51;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ006")
//    public String getQuestion52() {
//        return question52;
//    }
//
//    public void setQuestion52(String question52) {
//        this.question52 = question52;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ007")
//    public String getQuestion53() {
//        return question53;
//    }
//
//    public void setQuestion53(String question53) {
//        this.question53 = question53;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ008")
//    public String getQuestion54() {
//        return question54;
//    }
//
//    public void setQuestion54(String question54) {
//        this.question54 = question54;
//    }
//
//    @Basic
//    @Column(name = "975189X22X313SQ009")
//    public String getQuestion55() {
//        return question55;
//    }
//
//    public void setQuestion55(String question55) {
//        this.question55 = question55;
//    }
//
//    @Basic
//    @Column(name = "975189X22X315")
//    public String getQuestion56() {
//        return question56;
//    }
//
//    public void setQuestion56(String question56) {
//        this.question56 = question56;
//    }
//
//    @Basic
//    @Column(name = "975189X26X295")
//    public String getQuestion83() {
//        return question83;
//    }
//
//    public void setQuestion83(String question83) {
//        this.question83 = question83;
//    }
//
//    @Basic
//    @Column(name = "975189X26X305")
//    public String getQuestion87() {
//        return question87;
//    }
//
//    public void setQuestion87(String question87) {
//        this.question87 = question87;
//    }
//
//    @Basic
//    @Column(name = "975189X26X290")
//    public String getQuestion80() {
//        return question80;
//    }
//
//    public void setQuestion80(String question80) {
//        this.question80 = question80;
//    }
//
//    @Basic
//    @Column(name = "975189X26X301")
//    public String getQuestion84() {
//        return question84;
//    }
//
//    public void setQuestion84(String question84) {
//        this.question84 = question84;
//    }
//
//    @Basic
//    @Column(name = "975189X26X291")
//    public String getQuestion81() {
//        return question81;
//    }
//
//    public void setQuestion81(String question81) {
//        this.question81 = question81;
//    }
//
//    @Basic
//    @Column(name = "975189X26X302")
//    public String getQuestion85() {
//        return question85;
//    }
//
//    public void setQuestion85(String question85) {
//        this.question85 = question85;
//    }
//
//    @Basic
//    @Column(name = "975189X26X293")
//    public String getQuestion82() {
//        return question82;
//    }
//
//    public void setQuestion82(String question82) {
//        this.question82 = question82;
//    }
//
//    @Basic
//    @Column(name = "975189X26X303")
//    public String getQuestion86() {
//        return question86;
//    }
//
//    public void setQuestion86(String question86) {
//        this.question86 = question86;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ010")
//    public String getQuestion64() {
//        return question64;
//    }
//
//    public void setQuestion64(String question64) {
//        this.question64 = question64;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ011")
//    public String getQuestion65() {
//        return question65;
//    }
//
//    public void setQuestion65(String question65) {
//        this.question65 = question65;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ012")
//    public String getQuestion66() {
//        return question66;
//    }
//
//    public void setQuestion66(String question66) {
//        this.question66 = question66;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ001")
//    public String getQuestion57() {
//        return question57;
//    }
//
//    public void setQuestion57(String question57) {
//        this.question57 = question57;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ002")
//    public String getQuestion58() {
//        return question58;
//    }
//
//    public void setQuestion58(String question58) {
//        this.question58 = question58;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ003")
//    public String getQuestion59() {
//        return question59;
//    }
//
//    public void setQuestion59(String question59) {
//        this.question59 = question59;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ004")
//    public String getQuestion60() {
//        return question60;
//    }
//
//    public void setQuestion60(String question60) {
//        this.question60 = question60;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ005")
//    public String getQuestion61() {
//        return question61;
//    }
//
//    public void setQuestion61(String question61) {
//        this.question61 = question61;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ006")
//    public String getQuestion62() {
//        return question62;
//    }
//
//    public void setQuestion62(String question62) {
//        this.question62 = question62;
//    }
//
//    @Basic
//    @Column(name = "975189X23X300SQ007")
//    public String getQuestion63() {
//        return question63;
//    }
//
//    public void setQuestion63(String question63) {
//        this.question63 = question63;
//    }
//
//    @Basic
//    @Column(name = "975189X24X292")
//    public String getQuestion67() {
//        return question67;
//    }
//
//    public void setQuestion67(String question67) {
//        this.question67 = question67;
//    }
//
//    @Basic
//    @Column(name = "975189X24X314")
//    public String getQuestion73() {
//        return question73;
//    }
//
//    public void setQuestion73(String question73) {
//        this.question73 = question73;
//    }
//
//    @Basic
//    @Column(name = "975189X24X318")
//    public String getQuestion74() {
//        return question74;
//    }
//
//    public void setQuestion74(String question74) {
//        this.question74 = question74;
//    }
//
//    @Basic
//    @Column(name = "975189X24X307")
//    public String getQuestion68() {
//        return question68;
//    }
//
//    public void setQuestion68(String question68) {
//        this.question68 = question68;
//    }
//
//    @Basic
//    @Column(name = "975189X24X310SQ001")
//    public String getQuestion70() {
//        return question70;
//    }
//
//    public void setQuestion70(String question70) {
//        this.question70 = question70;
//    }
//
//    @Basic
//    @Column(name = "975189X24X310SQ01")
//    public String getQuestion69() {
//        return question69;
//    }
//
//    public void setQuestion69(String question69) {
//        this.question69 = question69;
//    }
//
//    @Basic
//    @Column(name = "975189X24X310SQ03")
//    public String getQuestion71() {
//        return question71;
//    }
//
//    public void setQuestion71(String question71) {
//        this.question71 = question71;
//    }
//
//    @Basic
//    @Column(name = "975189X24X310SQ04")
//    public String getQuestion72() {
//        return question72;
//    }
//
//    public void setQuestion72(String question72) {
//        this.question72 = question72;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        SurveyEntity that = (SurveyEntity) o;
//        return id == that.id &&
//                Objects.equals(token, that.token) &&
//                Objects.equals(submitdate, that.submitdate) &&
//                Objects.equals(lastpage, that.lastpage) &&
//                Objects.equals(startlanguage, that.startlanguage) &&
//                Objects.equals(seed, that.seed) &&
//                Objects.equals(startdate, that.startdate) &&
//                Objects.equals(datestamp, that.datestamp) &&
//                Objects.equals(ipaddr, that.ipaddr) &&
//                Objects.equals(question1, that.question1) &&
//                Objects.equals(question3, that.question3) &&
//                Objects.equals(question4, that.question4) &&
//                Objects.equals(question5, that.question5) &&
//                Objects.equals(question6, that.question6) &&
//                Objects.equals(question7, that.question7) &&
//                Objects.equals(question8, that.question8) &&
//                Objects.equals(question2, that.question2) &&
//                Objects.equals(question9, that.question9) &&
//                Objects.equals(question22, that.question22) &&
//                Objects.equals(question10, that.question10) &&
//                Objects.equals(question11, that.question11) &&
//                Objects.equals(question12, that.question12) &&
//                Objects.equals(question13, that.question13) &&
//                Objects.equals(question14, that.question14) &&
//                Objects.equals(question15, that.question15) &&
//                Objects.equals(question16, that.question16) &&
//                Objects.equals(question17, that.question17) &&
//                Objects.equals(question18, that.question18) &&
//                Objects.equals(question19, that.question19) &&
//                Objects.equals(question20, that.question20) &&
//                Objects.equals(question21, that.question21) &&
//                Objects.equals(question75, that.question75) &&
//                Objects.equals(question76, that.question76) &&
//                Objects.equals(question77, that.question77) &&
//                Objects.equals(question78, that.question78) &&
//                Objects.equals(question79, that.question79) &&
//                Objects.equals(question30, that.question30) &&
//                Objects.equals(question23, that.question23) &&
//                Objects.equals(question24, that.question24) &&
//                Objects.equals(question25, that.question25) &&
//                Objects.equals(question26, that.question26) &&
//                Objects.equals(question27, that.question27) &&
//                Objects.equals(question28, that.question28) &&
//                Objects.equals(question29, that.question29) &&
//                Objects.equals(question31, that.question31) &&
//                Objects.equals(question32, that.question32) &&
//                Objects.equals(question33, that.question33) &&
//                Objects.equals(question34, that.question34) &&
//                Objects.equals(question35, that.question35) &&
//                Objects.equals(question36, that.question36) &&
//                Objects.equals(question37, that.question37) &&
//                Objects.equals(question38, that.question38) &&
//                Objects.equals(question39, that.question39) &&
//                Objects.equals(question40, that.question40) &&
//                Objects.equals(question41, that.question41) &&
//                Objects.equals(question42, that.question42) &&
//                Objects.equals(question43, that.question43) &&
//                Objects.equals(question44, that.question44) &&
//                Objects.equals(question45, that.question45) &&
//                Objects.equals(question46, that.question46) &&
//                Objects.equals(question47, that.question47) &&
//                Objects.equals(question48, that.question48) &&
//                Objects.equals(question49, that.question49) &&
//                Objects.equals(question50, that.question50) &&
//                Objects.equals(question51, that.question51) &&
//                Objects.equals(question52, that.question52) &&
//                Objects.equals(question53, that.question53) &&
//                Objects.equals(question54, that.question54) &&
//                Objects.equals(question55, that.question55) &&
//                Objects.equals(question56, that.question56) &&
//                Objects.equals(question83, that.question83) &&
//                Objects.equals(question87, that.question87) &&
//                Objects.equals(question80, that.question80) &&
//                Objects.equals(question84, that.question84) &&
//                Objects.equals(question81, that.question81) &&
//                Objects.equals(question85, that.question85) &&
//                Objects.equals(question82, that.question82) &&
//                Objects.equals(question86, that.question86) &&
//                Objects.equals(question64, that.question64) &&
//                Objects.equals(question65, that.question65) &&
//                Objects.equals(question66, that.question66) &&
//                Objects.equals(question57, that.question57) &&
//                Objects.equals(question58, that.question58) &&
//                Objects.equals(question59, that.question59) &&
//                Objects.equals(question60, that.question60) &&
//                Objects.equals(question61, that.question61) &&
//                Objects.equals(question62, that.question62) &&
//                Objects.equals(question63, that.question63) &&
//                Objects.equals(question67, that.question67) &&
//                Objects.equals(question73, that.question73) &&
//                Objects.equals(question74, that.question74) &&
//                Objects.equals(question68, that.question68) &&
//                Objects.equals(question70, that.question70) &&
//                Objects.equals(question69, that.question69) &&
//                Objects.equals(question71, that.question71) &&
//                Objects.equals(question72, that.question72);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(id, token, submitdate, lastpage, startlanguage, seed, startdate, datestamp, ipaddr, question1, question3, question4, question5, question6, question7, question8, question2, question9, question22, question10, question11, question12, question13, question14, question15, question16, question17, question18, question19, question20, question21, question75, question76, question77, question78, question79, question30, question23, question24, question25, question26, question27, question28, question29, question31, question32, question33, question34, question35, question36, question37, question38, question39, question40, question41, question42, question43, question44, question45, question46, question47, question48, question49, question50, question51, question52, question53, question54, question55, question56, question83, question87, question80, question84, question81, question85, question82, question86, question64, question65, question66, question57, question58, question59, question60, question61, question62, question63, question67, question73, question74, question68, question70, question69, question71, question72);
//    }
//
//    public enum Criteria {
//        SCHOOL(0), CITY(1), AREA(2), POSTCODE(3);
//
//        private final int id;
//
//        Criteria(int id) {
//            this.id = id;
//        }
//
//        public static Criteria valueOf(int x) {
//            switch (x) {
//                case 0:
//                    return SCHOOL;
//                case 1:
//                    return CITY;
//                case 2:
//                    return AREA;
//                case 3:
//                    return POSTCODE;
//
//            }
//            return null;
//        }
//
//        public int getValue() {
//            return id;
//        }
//    }
//
//}
