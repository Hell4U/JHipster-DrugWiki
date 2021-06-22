package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.BrandType;
import com.mycompany.myapp.domain.enumeration.TypeUnit;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Brand.
 */
@Table("brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("bname")
    private String bname;

    @NotNull(message = "must not be null")
    @Column("price")
    private Double price;

    @NotNull(message = "must not be null")
    @Column("date")
    private LocalDate date;

    @NotNull(message = "must not be null")
    @Column("packageunit")
    private Double packageunit;

    @NotNull(message = "must not be null")
    @Column("type")
    private BrandType type;

    @NotNull(message = "must not be null")
    @Column("typeunit")
    private TypeUnit typeunit;

    @Transient
    private Company companyofMedicine;

    @Column("companyof_medicine_id")
    private Long companyofMedicineId;

    @JsonIgnoreProperties(value = { "ingredientName", "ids" }, allowSetters = true)
    @Transient
    private Set<Generics> generics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand id(Long id) {
        this.id = id;
        return this;
    }

    public String getBname() {
        return this.bname;
    }

    public Brand bname(String bname) {
        this.bname = bname;
        return this;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Double getPrice() {
        return this.price;
    }

    public Brand price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Brand date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPackageunit() {
        return this.packageunit;
    }

    public Brand packageunit(Double packageunit) {
        this.packageunit = packageunit;
        return this;
    }

    public void setPackageunit(Double packageunit) {
        this.packageunit = packageunit;
    }

    public BrandType getType() {
        return this.type;
    }

    public Brand type(BrandType type) {
        this.type = type;
        return this;
    }

    public void setType(BrandType type) {
        this.type = type;
    }

    public TypeUnit getTypeunit() {
        return this.typeunit;
    }

    public Brand typeunit(TypeUnit typeunit) {
        this.typeunit = typeunit;
        return this;
    }

    public void setTypeunit(TypeUnit typeunit) {
        this.typeunit = typeunit;
    }

    public Company getCompanyofMedicine() {
        return this.companyofMedicine;
    }

    public Brand companyofMedicine(Company company) {
        this.setCompanyofMedicine(company);
        this.companyofMedicineId = company != null ? company.getId() : null;
        return this;
    }

    public void setCompanyofMedicine(Company company) {
        this.companyofMedicine = company;
        this.companyofMedicineId = company != null ? company.getId() : null;
    }

    public Long getCompanyofMedicineId() {
        return this.companyofMedicineId;
    }

    public void setCompanyofMedicineId(Long company) {
        this.companyofMedicineId = company;
    }

    public Set<Generics> getGenerics() {
        return this.generics;
    }

    public Brand generics(Set<Generics> generics) {
        this.setGenerics(generics);
        return this;
    }

    public Brand addGenerics(Generics generics) {
        this.generics.add(generics);
        generics.getIds().add(this);
        return this;
    }

    public Brand removeGenerics(Generics generics) {
        this.generics.remove(generics);
        generics.getIds().remove(this);
        return this;
    }

    public void setGenerics(Set<Generics> generics) {
        this.generics = generics;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Brand)) {
            return false;
        }
        return id != null && id.equals(((Brand) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Brand{" +
            "id=" + getId() +
            ", bname='" + getBname() + "'" +
            ", price=" + getPrice() +
            ", date='" + getDate() + "'" +
            ", packageunit=" + getPackageunit() +
            ", type='" + getType() + "'" +
            ", typeunit='" + getTypeunit() + "'" +
            "}";
    }
}
