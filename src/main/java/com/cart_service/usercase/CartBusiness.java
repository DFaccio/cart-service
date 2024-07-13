package com.cart_service.usercase;

import com.cart_service.entities.Cart;
import com.cart_service.entities.ProductDetails;
import com.cart_service.entities.ProductReservation;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.util.enums.CartStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CartBusiness {

    public Cart save(String costumertId, ReservationDto reservationDto, ProductDto productDto){

        Cart cart = new Cart();
        ProductDetails productDetails = new ProductDetails();
        ProductReservation productReservation = new ProductReservation();
        List<ProductReservation> productReservationList = new ArrayList<>();

        int quantity = reservationDto.getQuantity();
        double productValue = productDto.getValue();

        productReservation.setReservationId(reservationDto.getId());

        productDetails.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
        productDetails.setName(productDto.getCategoryInformationDto().getName());
        productDetails.setPrice(productDto.getValue());
        productDetails.setQuantity(quantity);
        productDetails.setTotal(calculateProductValue(quantity, productValue));

        productReservation.setProductDetails(productDetails);

        productReservationList.add(productReservation);

        cart.setProductReservation(productReservationList);
        cart.setCostumerId(costumertId);
        cart.setProductsQuantity(calculateProductsQuantity(productReservationList));
        cart.setCartValue(calculateCartValue(productReservationList));
        cart.setCreationDate(LocalDateTime.now());
        cart.setUpdateDate(LocalDateTime.now());
        cart.setStatus(CartStatus.CREATED);

        return cart;

    }

    public Cart update(Cart cart, ReservationDto reservationDto, ProductDto productDto){

//    TODO - varrer lista do carrinho, se encontrar o sku
//        se for quantidade > 0, atualiza quantidade e valor.
//        se for quantidade = 0, remover a ocorrência do sku
//        se não encontrar, adiciona o novo sku
//        no final, recalcular quantidade e valor totais do carrinho

        return null;

    }

    private double calculateProductValue(int quantity, double value){

        return quantity * value;

    }

    private int calculateProductsQuantity(List<ProductReservation> productReservationList){

        int quantity = 0;

        for(ProductReservation productReservation : productReservationList){

            quantity += productReservation.getProductDetails().getQuantity();

        }

        return quantity;

    }

    private double calculateCartValue(List<ProductReservation> productReservationList){

        double value = 0;

        for(ProductReservation productReservation : productReservationList){

            value += productReservation.getProductDetails().getTotal();

        }

        return value;

    }
}
