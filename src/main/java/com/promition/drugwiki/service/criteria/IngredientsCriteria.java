package com.promition.drugwiki.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.promition.drugwiki.domain.Ingredients} entity. This class is used
 * in {@link com.promition.drugwiki.web.rest.IngredientsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ingredients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IngredientsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter iname;

    private StringFilter symptoms;

    private StringFilter sideeffects;

    private StringFilter cautions;

    public IngredientsCriteria() {}

    public IngredientsCriteria(IngredientsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.iname = other.iname == null ? null : other.iname.copy();
        this.symptoms = other.symptoms == null ? null : other.symptoms.copy();
        this.sideeffects = other.sideeffects == null ? null : other.sideeffects.copy();
        this.cautions = other.cautions == null ? null : other.cautions.copy();
    }

    @Override
    public IngredientsCriteria copy() {
        return new IngredientsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIname() {
        return iname;
    }

    public StringFilter iname() {
        if (iname == null) {
            iname = new StringFilter();
        }
        return iname;
    }

    public void setIname(StringFilter iname) {
        this.iname = iname;
    }

    public StringFilter getSymptoms() {
        return symptoms;
    }

    public StringFilter symptoms() {
        if (symptoms == null) {
            symptoms = new StringFilter();
        }
        return symptoms;
    }

    public void setSymptoms(StringFilter symptoms) {
        this.symptoms = symptoms;
    }

    public StringFilter getSideeffects() {
        return sideeffects;
    }

    public StringFilter sideeffects() {
        if (sideeffects == null) {
            sideeffects = new StringFilter();
        }
        return sideeffects;
    }

    public void setSideeffects(StringFilter sideeffects) {
        this.sideeffects = sideeffects;
    }

    public StringFilter getCautions() {
        return cautions;
    }

    public StringFilter cautions() {
        if (cautions == null) {
            cautions = new StringFilter();
        }
        return cautions;
    }

    public void setCautions(StringFilter cautions) {
        this.cautions = cautions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IngredientsCriteria that = (IngredientsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(iname, that.iname) &&
            Objects.equals(symptoms, that.symptoms) &&
            Objects.equals(sideeffects, that.sideeffects) &&
            Objects.equals(cautions, that.cautions)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iname, symptoms, sideeffects, cautions);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (iname != null ? "iname=" + iname + ", " : "") +
            (symptoms != null ? "symptoms=" + symptoms + ", " : "") +
            (sideeffects != null ? "sideeffects=" + sideeffects + ", " : "") +
            (cautions != null ? "cautions=" + cautions + ", " : "") +
            "}";
    }
}
