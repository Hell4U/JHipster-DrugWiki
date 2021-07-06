package com.promition.drugwiki.service.dto;

import com.promition.drugwiki.domain.enumeration.DosageUnit;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.promition.drugwiki.domain.Generics} entity.
 */
public class GenericsDTO implements Serializable {

    private Long id;

    @NotNull
    private String gname;

    @NotNull
    private Double dosage;

    @NotNull
    private DosageUnit dosageunit;

    private IngredientsDTO ingredientsused;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Double getDosage() {
        return dosage;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public DosageUnit getDosageunit() {
        return dosageunit;
    }

    public void setDosageunit(DosageUnit dosageunit) {
        this.dosageunit = dosageunit;
    }

    public IngredientsDTO getIngredientsused() {
        return ingredientsused;
    }

    public void setIngredientsused(IngredientsDTO ingredientsused) {
        this.ingredientsused = ingredientsused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenericsDTO)) {
            return false;
        }

        GenericsDTO genericsDTO = (GenericsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, genericsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenericsDTO{" +
            "id=" + getId() +
            ", gname='" + getGname() + "'" +
            ", dosage=" + getDosage() +
            ", dosageunit='" + getDosageunit() + "'" +
            ", ingredientsused=" + getIngredientsused() +
            "}";
    }
}
