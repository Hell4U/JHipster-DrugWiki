package com.promition.drugwiki.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.promition.drugwiki.domain.Ingredients} entity.
 */
public class IngredientsDTO implements Serializable {

    private Long id;

    @NotNull
    private String iname;

    @NotNull
    private String symptoms;

    @NotNull
    private String sideeffects;

    @NotNull
    private String cautions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSideeffects() {
        return sideeffects;
    }

    public void setSideeffects(String sideeffects) {
        this.sideeffects = sideeffects;
    }

    public String getCautions() {
        return cautions;
    }

    public void setCautions(String cautions) {
        this.cautions = cautions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientsDTO)) {
            return false;
        }

        IngredientsDTO ingredientsDTO = (IngredientsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingredientsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientsDTO{" +
            "id=" + getId() +
            ", iname='" + getIname() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            ", sideeffects='" + getSideeffects() + "'" +
            ", cautions='" + getCautions() + "'" +
            "}";
    }
}
