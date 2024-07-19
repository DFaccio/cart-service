package com.cart_service.interfaceadapters.presenters.dto.cart;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.cart_service.util.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductDetailsDto extends Dto implements Serializable {

    @NotBlank
    @Schema(example = "CLOTHES-SOME-JEANS-JEANS-BRAND-YELLOW-P")
    private String sku;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private double price;

    @NotNull
    @Schema(example = "5")
    private int quantity;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private double total;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private ReservationStatus status;

}
