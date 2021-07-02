package com.promition.drugwiki.service.criteria;

import com.promition.drugwiki.domain.enumeration.BrandType;
import com.promition.drugwiki.domain.enumeration.TypeUnit;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.promition.drugwiki.domain.Brand} entity. This class is used
 * in {@link com.promition.drugwiki.web.rest.BrandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /brands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BrandCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BrandType
     */
    public static class BrandTypeFilter extends Filter<BrandType> {

        public BrandTypeFilter() {}

        public BrandTypeFilter(BrandTypeFilter filter) {
            super(filter);
        }

        @Override
        public BrandTypeFilter copy() {
            return new BrandTypeFilter(this);
        }
    }

    /**
     * Class for filtering TypeUnit
     */
    public static class TypeUnitFilter extends Filter<TypeUnit> {

        public TypeUnitFilter() {}

        public TypeUnitFilter(TypeUnitFilter filter) {
            super(filter);
        }

        @Override
        public TypeUnitFilter copy() {
            return new TypeUnitFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bname;

    private DoubleFilter price;

    private LocalDateFilter date;

    private DoubleFilter packageunit;

    private BrandTypeFilter type;

    private TypeUnitFilter typeunit;

    private LongFilter companyofMedicineId;

    private LongFilter genericsusedId;

    public BrandCriteria() {}

    public BrandCriteria(BrandCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bname = other.bname == null ? null : other.bname.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.packageunit = other.packageunit == null ? null : other.packageunit.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.typeunit = other.typeunit == null ? null : other.typeunit.copy();
        this.companyofMedicineId = other.companyofMedicineId == null ? null : other.companyofMedicineId.copy();
        this.genericsusedId = other.genericsusedId == null ? null : other.genericsusedId.copy();
    }

    @Override
    public BrandCriteria copy() {
        return new BrandCriteria(this);
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

    public StringFilter getBname() {
        return bname;
    }

    public StringFilter bname() {
        if (bname == null) {
            bname = new StringFilter();
        }
        return bname;
    }

    public void setBname(StringFilter bname) {
        this.bname = bname;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public DoubleFilter getPackageunit() {
        return packageunit;
    }

    public DoubleFilter packageunit() {
        if (packageunit == null) {
            packageunit = new DoubleFilter();
        }
        return packageunit;
    }

    public void setPackageunit(DoubleFilter packageunit) {
        this.packageunit = packageunit;
    }

    public BrandTypeFilter getType() {
        return type;
    }

    public BrandTypeFilter type() {
        if (type == null) {
            type = new BrandTypeFilter();
        }
        return type;
    }

    public void setType(BrandTypeFilter type) {
        this.type = type;
    }

    public TypeUnitFilter getTypeunit() {
        return typeunit;
    }

    public TypeUnitFilter typeunit() {
        if (typeunit == null) {
            typeunit = new TypeUnitFilter();
        }
        return typeunit;
    }

    public void setTypeunit(TypeUnitFilter typeunit) {
        this.typeunit = typeunit;
    }

    public LongFilter getCompanyofMedicineId() {
        return companyofMedicineId;
    }

    public LongFilter companyofMedicineId() {
        if (companyofMedicineId == null) {
            companyofMedicineId = new LongFilter();
        }
        return companyofMedicineId;
    }

    public void setCompanyofMedicineId(LongFilter companyofMedicineId) {
        this.companyofMedicineId = companyofMedicineId;
    }

    public LongFilter getGenericsusedId() {
        return genericsusedId;
    }

    public LongFilter genericsusedId() {
        if (genericsusedId == null) {
            genericsusedId = new LongFilter();
        }
        return genericsusedId;
    }

    public void setGenericsusedId(LongFilter genericsusedId) {
        this.genericsusedId = genericsusedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BrandCriteria that = (BrandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bname, that.bname) &&
            Objects.equals(price, that.price) &&
            Objects.equals(date, that.date) &&
            Objects.equals(packageunit, that.packageunit) &&
            Objects.equals(type, that.type) &&
            Objects.equals(typeunit, that.typeunit) &&
            Objects.equals(companyofMedicineId, that.companyofMedicineId) &&
            Objects.equals(genericsusedId, that.genericsusedId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bname, price, date, packageunit, type, typeunit, companyofMedicineId, genericsusedId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BrandCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bname != null ? "bname=" + bname + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (packageunit != null ? "packageunit=" + packageunit + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (typeunit != null ? "typeunit=" + typeunit + ", " : "") +
            (companyofMedicineId != null ? "companyofMedicineId=" + companyofMedicineId + ", " : "") +
            (genericsusedId != null ? "genericsusedId=" + genericsusedId + ", " : "") +
            "}";
    }
}
