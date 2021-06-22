package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Ingredients.
 */
@Table("ingredients")
public class Ingredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("iname")
    private String iname;

    @NotNull(message = "must not be null")
    @Column("symptoms")
    private String symptoms;

    @NotNull(message = "must not be null")
    @Column("sideeffects")
    private String sideeffects;

    @NotNull(message = "must not be null")
    @Column("cautions")
    private String cautions;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingredients id(Long id) {
        this.id = id;
        return this;
    }

    public String getIname() {
        return this.iname;
    }

    public Ingredients iname(String iname) {
        this.iname = iname;
        return this;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getSymptoms() {
        return this.symptoms;
    }

    public Ingredients symptoms(String symptoms) {
        this.symptoms = symptoms;
        return this;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSideeffects() {
        return this.sideeffects;
    }

    public Ingredients sideeffects(String sideeffects) {
        this.sideeffects = sideeffects;
        return this;
    }

    public void setSideeffects(String sideeffects) {
        this.sideeffects = sideeffects;
    }

    public String getCautions() {
        return this.cautions;
    }

    public Ingredients cautions(String cautions) {
        this.cautions = cautions;
        return this;
    }

    public void setCautions(String cautions) {
        this.cautions = cautions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingredients)) {
            return false;
        }
        return id != null && id.equals(((Ingredients) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingredients{" +
            "id=" + getId() +
            ", iname='" + getIname() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            ", sideeffects='" + getSideeffects() + "'" +
            ", cautions='" + getCautions() + "'" +
            "}";
    }
}
