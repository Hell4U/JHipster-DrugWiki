package com.promition.drugwiki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The Company entity.\n@author A true hipster
 */
@ApiModel(description = "The Company entity.\n@author A true hipster")
@Table("company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    /**
     * fieldName
     */
    @NotNull(message = "must not be null")
    @ApiModelProperty(value = "fieldName", required = true)
    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Transient
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<Brand> brands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Company address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Brand> getBrands() {
        return this.brands;
    }

    public Company brands(Set<Brand> brands) {
        this.setBrands(brands);
        return this;
    }

    public Company addBrand(Brand brand) {
        this.brands.add(brand);
        brand.setCompany(this);
        return this;
    }

    public Company removeBrand(Brand brand) {
        this.brands.remove(brand);
        brand.setCompany(null);
        return this;
    }

    public void setBrands(Set<Brand> brands) {
        if (this.brands != null) {
            this.brands.forEach(i -> i.setCompany(null));
        }
        if (brands != null) {
            brands.forEach(i -> i.setCompany(this));
        }
        this.brands = brands;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
