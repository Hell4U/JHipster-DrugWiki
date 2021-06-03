package com.promition.drugwiki.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.promition.drugwiki.domain.Company} entity.
 */
@ApiModel(description = "The Company entity.\n@author A true hipster")
public class CompanyDTO implements Serializable {

    private Long id;

    /**
     * fieldName
     */
    @NotNull(message = "must not be null")
    @ApiModelProperty(value = "fieldName", required = true)
    private String name;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
