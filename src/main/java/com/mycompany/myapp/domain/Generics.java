package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DosageUnit;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Generics.
 */
@Table("generics")
public class Generics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("dosage")
    private Double dosage;

    @NotNull(message = "must not be null")
    @Column("dosageunit")
    private DosageUnit dosageunit;

    private Long ingredientNameId;

    @Transient
    private Ingredients ingredientName;

    @JsonIgnoreProperties(value = { "companyofMedicine", "generics" }, allowSetters = true)
    @Transient
    private Set<Brand> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Generics id(Long id) {
        this.id = id;
        return this;
    }

    public Double getDosage() {
        return this.dosage;
    }

    public Generics dosage(Double dosage) {
        this.dosage = dosage;
        return this;
    }

    public void setDosage(Double dosage) {
        this.dosage = dosage;
    }

    public DosageUnit getDosageunit() {
        return this.dosageunit;
    }

    public Generics dosageunit(DosageUnit dosageunit) {
        this.dosageunit = dosageunit;
        return this;
    }

    public void setDosageunit(DosageUnit dosageunit) {
        this.dosageunit = dosageunit;
    }

    public Ingredients getIngredientName() {
        return this.ingredientName;
    }

    public Generics ingredientName(Ingredients ingredients) {
        this.setIngredientName(ingredients);
        this.ingredientNameId = ingredients != null ? ingredients.getId() : null;
        return this;
    }

    public void setIngredientName(Ingredients ingredients) {
        this.ingredientName = ingredients;
        this.ingredientNameId = ingredients != null ? ingredients.getId() : null;
    }

    public Long getIngredientNameId() {
        return this.ingredientNameId;
    }

    public void setIngredientNameId(Long ingredients) {
        this.ingredientNameId = ingredients;
    }

    public Set<Brand> getIds() {
        return this.ids;
    }

    public Generics ids(Set<Brand> brands) {
        this.setIds(brands);
        return this;
    }

    public Generics addId(Brand brand) {
        this.ids.add(brand);
        brand.getGenerics().add(this);
        return this;
    }

    public Generics removeId(Brand brand) {
        this.ids.remove(brand);
        brand.getGenerics().remove(this);
        return this;
    }

    public void setIds(Set<Brand> brands) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.removeGenerics(this));
        }
        if (brands != null) {
            brands.forEach(i -> i.addGenerics(this));
        }
        this.ids = brands;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Generics)) {
            return false;
        }
        return id != null && id.equals(((Generics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Generics{" +
            "id=" + getId() +
            ", dosage=" + getDosage() +
            ", dosageunit='" + getDosageunit() + "'" +
            "}";
    }
}
