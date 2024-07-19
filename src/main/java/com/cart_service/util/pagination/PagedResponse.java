package com.cart_service.util.pagination;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponse<T extends Dto> {

    private Pagination page;

    private List<T> data;

}