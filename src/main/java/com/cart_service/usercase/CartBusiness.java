package com.cart_service.usercase;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class CartBusiness {

    public Cart save(String costumertId, ReservationDto reservationDto, ProductDto productDto){


        return null;

    }

    public Cart update(Cart cart, ReservationDto reservationDto, ProductDto productDto){

//    TODO - varrer lista do carrinho, se encontrar o sku
//        se for quantidade > 0, atualiza quantidade e valor.
//        se for quantidade = 0, remover a ocorrência do sku
//        se não encontrar, adiciona o novo sku
//        no final, recalcular quantidade e valor totais do carrinho

        return null;

    }

}
