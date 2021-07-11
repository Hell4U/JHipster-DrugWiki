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
 * Criteria class for the {@link com.promition.drugwiki.domain.Company} entity. This class is used
 * in {@link com.promition.drugwiki.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cname;

    private StringFilter address;

    private StringFilter website;

    private StringFilter email;

    private StringFilter fax;

    private StringFilter phoneno;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cname = other.cname == null ? null : other.cname.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.fax = other.fax == null ? null : other.fax.copy();
        this.phoneno = other.phoneno == null ? null : other.phoneno.copy();
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public StringFilter getCname() {
        return cname;
    }

    public StringFilter cname() {
        if (cname == null) {
            cname = new StringFilter();
        }
        return cname;
    }

    public void setCname(StringFilter cname) {
        this.cname = cname;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public StringFilter website() {
        if (website == null) {
            website = new StringFilter();
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getFax() {
        return fax;
    }

    public StringFilter fax() {
        if (fax == null) {
            fax = new StringFilter();
        }
        return fax;
    }

    public void setFax(StringFilter fax) {
        this.fax = fax;
    }

    public StringFilter getPhoneno() {
        return phoneno;
    }

    public StringFilter phoneno() {
        if (phoneno == null) {
            phoneno = new StringFilter();
        }
        return phoneno;
    }

    public void setPhoneno(StringFilter phoneno) {
        this.phoneno = phoneno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cname, that.cname) &&
            Objects.equals(address, that.address) &&
            Objects.equals(website, that.website) &&
            Objects.equals(email, that.email) &&
            Objects.equals(fax, that.fax) &&
            Objects.equals(phoneno, that.phoneno)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cname, address, website, email, fax, phoneno);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cname != null ? "cname=" + cname + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (website != null ? "website=" + website + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (fax != null ? "fax=" + fax + ", " : "") +
            (phoneno != null ? "phoneno=" + phoneno + ", " : "") +
            "}";
    }
}
