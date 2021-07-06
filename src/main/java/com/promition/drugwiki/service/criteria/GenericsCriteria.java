package com.promition.drugwiki.service.criteria;

import com.promition.drugwiki.domain.enumeration.DosageUnit;
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
 * Criteria class for the {@link com.promition.drugwiki.domain.Generics} entity. This class is used
 * in {@link com.promition.drugwiki.web.rest.GenericsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /generics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GenericsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DosageUnit
     */
    public static class DosageUnitFilter extends Filter<DosageUnit> {

        public DosageUnitFilter() {}

        public DosageUnitFilter(DosageUnitFilter filter) {
            super(filter);
        }

        @Override
        public DosageUnitFilter copy() {
            return new DosageUnitFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter gname;

    private DoubleFilter dosage;

    private DosageUnitFilter dosageunit;

    private LongFilter ingredientsusedId;

    private LongFilter idId;

    public GenericsCriteria() {}

    public GenericsCriteria(GenericsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.gname = other.gname == null ? null : other.gname.copy();
        this.dosage = other.dosage == null ? null : other.dosage.copy();
        this.dosageunit = other.dosageunit == null ? null : other.dosageunit.copy();
        this.ingredientsusedId = other.ingredientsusedId == null ? null : other.ingredientsusedId.copy();
        this.idId = other.idId == null ? null : other.idId.copy();
    }

    @Override
    public GenericsCriteria copy() {
        return new GenericsCriteria(this);
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

    public StringFilter getGname() {
        return gname;
    }

    public StringFilter gname() {
        if (gname == null) {
            gname = new StringFilter();
        }
        return gname;
    }

    public void setGname(StringFilter gname) {
        this.gname = gname;
    }

    public DoubleFilter getDosage() {
        return dosage;
    }

    public DoubleFilter dosage() {
        if (dosage == null) {
            dosage = new DoubleFilter();
        }
        return dosage;
    }

    public void setDosage(DoubleFilter dosage) {
        this.dosage = dosage;
    }

    public DosageUnitFilter getDosageunit() {
        return dosageunit;
    }

    public DosageUnitFilter dosageunit() {
        if (dosageunit == null) {
            dosageunit = new DosageUnitFilter();
        }
        return dosageunit;
    }

    public void setDosageunit(DosageUnitFilter dosageunit) {
        this.dosageunit = dosageunit;
    }

    public LongFilter getIngredientsusedId() {
        return ingredientsusedId;
    }

    public LongFilter ingredientsusedId() {
        if (ingredientsusedId == null) {
            ingredientsusedId = new LongFilter();
        }
        return ingredientsusedId;
    }

    public void setIngredientsusedId(LongFilter ingredientsusedId) {
        this.ingredientsusedId = ingredientsusedId;
    }

    public LongFilter getIdId() {
        return idId;
    }

    public LongFilter idId() {
        if (idId == null) {
            idId = new LongFilter();
        }
        return idId;
    }

    public void setIdId(LongFilter idId) {
        this.idId = idId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GenericsCriteria that = (GenericsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gname, that.gname) &&
            Objects.equals(dosage, that.dosage) &&
            Objects.equals(dosageunit, that.dosageunit) &&
            Objects.equals(ingredientsusedId, that.ingredientsusedId) &&
            Objects.equals(idId, that.idId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gname, dosage, dosageunit, ingredientsusedId, idId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenericsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (gname != null ? "gname=" + gname + ", " : "") +
            (dosage != null ? "dosage=" + dosage + ", " : "") +
            (dosageunit != null ? "dosageunit=" + dosageunit + ", " : "") +
            (ingredientsusedId != null ? "ingredientsusedId=" + ingredientsusedId + ", " : "") +
            (idId != null ? "idId=" + idId + ", " : "") +
            "}";
    }
}
