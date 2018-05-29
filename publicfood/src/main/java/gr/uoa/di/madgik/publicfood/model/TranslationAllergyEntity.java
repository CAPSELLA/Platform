package gr.uoa.di.madgik.publicfood.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "translation_allergy", schema = "food_pilot", catalog = "")
public class TranslationAllergyEntity {
    private int id;
    private String language;
    private String text;
    private int allergyIdallergy;
    private String allergyDomainName;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "allergy_idallergy")
    public int getAllergyIdallergy() {
        return allergyIdallergy;
    }

    public void setAllergyIdallergy(int allergyIdallergy) {
        this.allergyIdallergy = allergyIdallergy;
    }

    @Basic
    @Column(name = "allergy_Domain_name")
    public String getAllergyDomainName() {
        return allergyDomainName;
    }

    public void setAllergyDomainName(String allergyDomainName) {
        this.allergyDomainName = allergyDomainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationAllergyEntity that = (TranslationAllergyEntity) o;
        return id == that.id &&
                allergyIdallergy == that.allergyIdallergy &&
                Objects.equals(language, that.language) &&
                Objects.equals(text, that.text) &&
                Objects.equals(allergyDomainName, that.allergyDomainName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, language, text, allergyIdallergy, allergyDomainName);
    }
}
