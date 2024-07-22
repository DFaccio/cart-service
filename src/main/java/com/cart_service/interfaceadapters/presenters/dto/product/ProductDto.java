package com.cart_service.interfaceadapters.presenters.dto.product;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto extends Dto implements Serializable {

    @JsonProperty("description")
    private String description;

    @JsonProperty("value")
    private double value;

    @JsonProperty("category_information")
    private CategoryInformationDto categoryInformationDto;

}
