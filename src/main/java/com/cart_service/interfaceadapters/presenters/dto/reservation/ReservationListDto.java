package com.cart_service.interfaceadapters.presenters.dto.reservation;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"id"})
public class ReservationListDto extends Dto implements Serializable {

    private List<ReservationDto> reservations;

}
