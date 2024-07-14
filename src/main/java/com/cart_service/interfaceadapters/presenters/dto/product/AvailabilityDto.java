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
@JsonIgnoreProperties({"id"})
public class AvailabilityDto extends Dto implements Serializable {

    @JsonProperty("sku")
    private String sku;

}
