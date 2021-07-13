package com.promition.drugwiki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.promition.drugwiki.domain.enumeration.DosageUnit;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Generics.
 */
@Entity
@Table(name = "generics")
public class Generics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "gname", nullable = false)
    private String gname;

    @NotNull
    @Column(name = "dosage", nullable = false)
    private Double dosage;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dosageunit", nullable = false)
    private DosageUnit dosageunit;

    @ManyToOne(optional = false)
    @NotNull
    private Ingredients ingredientsused;

    @ManyToMany(mappedBy = "genericsuseds")
    @JsonIgnoreProperties(value = { "companyofMedicine", "genericsuseds" }, allowSetters = true)
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

    public String getGname() {
        return this.gname;
    }

    public Generics gname(String gname) {
        this.gname = gname;
        return this;
    }

    public void setGname(String gname) {
        this.gname = gname;
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

    public Ingredients getIngredientsused() {
        return this.ingredientsused;
    }

    public Generics ingredientsused(Ingredients ingredients) {
        this.setIngredientsused(ingredients);
        return this;
    }

    public void setIngredientsused(Ingredients ingredients) {
        this.ingredientsused = ingredients;
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
        brand.getGenericsuseds().add(this);
        return this;
    }

    public Generics removeId(Brand brand) {
        this.ids.remove(brand);
        brand.getGenericsuseds().remove(this);
        return this;
    }

    public void setIds(Set<Brand> brands) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.removeGenericsused(this));
        }
        if (brands != null) {
            brands.forEach(i -> i.addGenericsused(this));
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
            ", gname='" + getGname() + "'" +
            ", dosage=" + getDosage() +
            ", dosageunit='" + getDosageunit() + "'" +
            "}";
    }
}
