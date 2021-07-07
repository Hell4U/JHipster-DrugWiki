package com.promition.drugwiki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.promition.drugwiki.domain.enumeration.BrandType;
import com.promition.drugwiki.domain.enumeration.TypeUnit;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Brand.
 */
@Entity
@Table(name = "brand")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bname", nullable = false)
    private String bname;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "packageunit", nullable = false)
    private Double packageunit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BrandType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "typeunit", nullable = false)
    private TypeUnit typeunit;

    @ManyToOne(optional = false)
    @NotNull
    private Company companyofMedicine;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_brand__genericsused",
        joinColumns = @JoinColumn(name = "brand_id"),
        inverseJoinColumns = @JoinColumn(name = "genericsused_id")
    )
    @JsonIgnoreProperties(value = { "ingredientsused", "ids" }, allowSetters = true)
    private Set<Generics> genericsuseds = new HashSet<>();

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
        return this;
    }

    public void setCompanyofMedicine(Company company) {
        this.companyofMedicine = company;
    }

    public Set<Generics> getGenericsuseds() {
        return this.genericsuseds;
    }

    public Brand genericsuseds(Set<Generics> generics) {
        this.setGenericsuseds(generics);
        return this;
    }

    public Brand addGenericsused(Generics generics) {
        this.genericsuseds.add(generics);
        generics.getIds().add(this);
        return this;
    }

    public Brand removeGenericsused(Generics generics) {
        this.genericsuseds.remove(generics);
        generics.getIds().remove(this);
        return this;
    }

    public void setGenericsuseds(Set<Generics> generics) {
        this.genericsuseds = generics;
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
