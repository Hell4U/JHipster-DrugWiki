package com.promition.drugwiki.service.dto;

import com.promition.drugwiki.domain.enumeration.BrandType;
import com.promition.drugwiki.domain.enumeration.TypeUnit;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.promition.drugwiki.domain.Brand} entity.
 */
public class BrandDTO implements Serializable {

    private Long id;

    @NotNull
    private String bname;

    @NotNull
    private Double price;

    @NotNull
    private LocalDate date;

    @NotNull
    private Double packageunit;

    @NotNull
    private BrandType type;

    @NotNull
    private TypeUnit typeunit;

    private CompanyDTO companyofMedicine;

    private Set<GenericsDTO> genericsuseds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPackageunit() {
        return packageunit;
    }

    public void setPackageunit(Double packageunit) {
        this.packageunit = packageunit;
    }

    public BrandType getType() {
        return type;
    }

    public void setType(BrandType type) {
        this.type = type;
    }

    public TypeUnit getTypeunit() {
        return typeunit;
    }

    public void setTypeunit(TypeUnit typeunit) {
        this.typeunit = typeunit;
    }

    public CompanyDTO getCompanyofMedicine() {
        return companyofMedicine;
    }

    public void setCompanyofMedicine(CompanyDTO companyofMedicine) {
        this.companyofMedicine = companyofMedicine;
    }

    public Set<GenericsDTO> getGenericsuseds() {
        return genericsuseds;
    }

    public void setGenericsuseds(Set<GenericsDTO> genericsuseds) {
        this.genericsuseds = genericsuseds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrandDTO)) {
            return false;
        }

        BrandDTO brandDTO = (BrandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, brandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BrandDTO{" +
            "id=" + getId() +
            ", bname='" + getBname() + "'" +
            ", price=" + getPrice() +
            ", date='" + getDate() + "'" +
            ", packageunit=" + getPackageunit() +
            ", type='" + getType() + "'" +
            ", typeunit='" + getTypeunit() + "'" +
            ", companyofMedicine=" + getCompanyofMedicine() +
            ", genericsuseds=" + getGenericsuseds() +
            "}";
    }
}
