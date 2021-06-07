package com.promition.drugwiki.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.promition.drugwiki.domain.Brand} entity.
 */
@ApiModel(description = "The Brand entity.\n@author A true hipster")
public class BrandDTO implements Serializable {

    private Long id;

    /**
     * name
     */
    @NotNull(message = "must not be null")
    @ApiModelProperty(value = "name", required = true)
    private String name;

    /**
     * price
     */
    @ApiModelProperty(value = "price")
    private Float price;

    @Size(max = 1000)
    private String description;

    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
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
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", company=" + getCompany() +
            "}";
    }
}
