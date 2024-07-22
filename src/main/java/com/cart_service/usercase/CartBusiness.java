package com.cart_service.usercase;

import com.cart_service.entities.Cart;
import com.cart_service.entities.ProductDetails;
import com.cart_service.entities.ProductReservation;
import com.cart_service.interfaceadapters.helper.CartHelper;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.cart.ProductReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.enums.ReservationStatus;
import com.cart_service.util.exceptions.ValidationsException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CartBusiness {

    @Resource
    private CartHelper cartHelper;

    public Cart addProductToCart(Mono<Cart> optional, String customerId, ReservationListDto reservationDto) throws ValidationsException {

        Cart cart;

        if (optional.blockOptional().isPresent()) {

            cart = addProduct(optional.blockOptional().get(), reservationDto);

        } else {

            cart = createCart(customerId, reservationDto);

        }

        return cart;

    }

    public Cart createCart(String customerId, ReservationListDto reservationListDto) throws ValidationsException {

        Cart cart = new Cart();
        List<ProductReservation> productReservationList = new ArrayList<>();

        reservationListDto = cartHelper.createReservation(reservationListDto);

        boolean hasReservation = false;

        for (ReservationDto reservationDto : reservationListDto.getReservations()) {

            ProductDetails productDetails = new ProductDetails();
            ProductReservation productReservation = new ProductReservation();

            ProductDto productDto = cartHelper.getProduct(reservationDto.getSku());

            int quantity = reservationDto.getQuantity();
            double productValue = productDto.getValue();
            ReservationStatus reservationStatus = reservationDto.getStatus();

            productDetails.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
            productDetails.setName(productDto.getCategoryInformationDto().getName());
            productDetails.setPrice(productValue);
            productDetails.setQuantity(quantity);
            productDetails.setStatus(reservationStatus);

            if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                hasReservation = true;

                productReservation.setReservationId(reservationDto.getId());
                productDetails.setTotal(calculateProductValue(quantity, productValue));

            } else if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.STOCKOUT.toString())) {

                productDetails.setTotal(0.0);

            }

            productReservation.setProductDetails(productDetails);
            productReservationList.add(productReservation);

        }

        if (hasReservation) {

            cart.setProductReservation(productReservationList);
            cart.setCustomerId(customerId);
            cart.setProductsQuantity(calculateProductsQuantity(productReservationList));
            cart.setCartValue(calculateCartValue(productReservationList));
            cart.setCreationDate(LocalDateTime.now());
            cart.setUpdateDate(LocalDateTime.now());
            cart.setCartStatus(CartStatus.CREATED);

        } else {

            throw new ValidationsException("0100");

        }

        return cart;

    }

    public Cart addProduct(Cart cart, ReservationListDto reservationListDto) throws ValidationsException {

        List<ReservationDto> reservations = reservationListDto.getReservations();

        List<ProductReservation> productReservationList = cart.getProductReservation();
        List<ReservationDto> reservationsToMake = new ArrayList<>();

        if(!StringUtils.equals(cart.getCartStatus().toString(), CartStatus.CREATED.toString())){

            throw new ValidationsException("0004");

        }

        boolean makeReservation = false;

        for (ProductReservation productReservation : productReservationList) {

            String cartSku = productReservation.getProductDetails().getSku();

            for (ReservationDto reservationDto : reservations) {

                String sku = reservationDto.getSku();

                if (StringUtils.equals(cartSku, sku)) {

                    ProductDetails productDetails = productReservation.getProductDetails();

                    reservationDto.setId(productReservation.getReservationId());

                    int quantity = productReservation.getProductDetails().getQuantity() + reservationDto.getQuantity();
                    double productValue = productReservation.getProductDetails().getPrice();

                    reservationDto.setQuantity(quantity);

                    reservationDto = cartHelper.updateReservation(reservationDto);

                    ReservationStatus reservationStatus = reservationDto.getStatus();

                    productDetails.setStatus(reservationStatus);

                    if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                        productDetails.setQuantity(quantity);
                        productDetails.setTotal(calculateProductValue(quantity, productValue));

                    }

                    productReservation.setProductDetails(productDetails);

                } else {

                    makeReservation = true;
                    reservationsToMake.add(reservationDto);

                }

            }

        }

        if (makeReservation) {

            ReservationListDto makeReservationList = new ReservationListDto();

            makeReservationList.setReservations(reservationsToMake);

            makeReservationList = cartHelper.createReservation(makeReservationList);

            for (ReservationDto reservationDto : makeReservationList.getReservations()) {

                ProductDetails productDetails = new ProductDetails();
                ProductReservation productReservation = new ProductReservation();

                ProductDto productDto = cartHelper.getProduct(reservationDto.getSku());

                int quantity = reservationDto.getQuantity();
                double productValue = productDto.getValue();
                ReservationStatus reservationStatus = reservationDto.getStatus();

                productDetails.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
                productDetails.setName(productDto.getCategoryInformationDto().getName());
                productDetails.setPrice(productValue);
                productDetails.setQuantity(quantity);
                productDetails.setStatus(reservationStatus);

                if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                    productReservation.setReservationId(reservationDto.getId());
                    productDetails.setTotal(calculateProductValue(quantity, productValue));

                } else if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.STOCKOUT.toString())) {

                    productDetails.setTotal(0.0);

                }

                productReservation.setProductDetails(productDetails);
                productReservationList.add(productReservation);

            }

        }

        cart.setProductReservation(productReservationList);
        cart.setProductsQuantity(calculateProductsQuantity(productReservationList));
        cart.setCartValue(calculateCartValue(productReservationList));
        cart.setUpdateDate(LocalDateTime.now());

        return cart;

    }

    public Cart updateCart(CartDto cartDto, Mono<Cart> optional) throws ValidationsException {

        Cart cart;

        if(optional.blockOptional().isPresent()){

            cart = optional.blockOptional().get();

        }else{

            throw new ValidationsException("0001");

        }

        if(!StringUtils.equals(cart.getCartStatus().toString(), CartStatus.CREATED.toString())){

            throw new ValidationsException("0004");

        }

        List<ProductReservation> reservedProductsList = cart.getProductReservation();
        List<ProductReservationDto> updateProductsList = cartDto.getProductReservation();

        for (ProductReservation productReservation : reservedProductsList) {

            String reservedSku = productReservation.getProductDetails().getSku();
            int reservedQuantity = productReservation.getProductDetails().getQuantity();

            for (ProductReservationDto updateProducts : updateProductsList) {

                String updatedSku = updateProducts.getProductDetails().getSku();
                int updateQuantity = updateProducts.getProductDetails().getQuantity();

                if (StringUtils.equals(reservedSku, updatedSku) || reservedQuantity != updateQuantity) {

                    ReservationDto reservationDto = new ReservationDto();
                    ProductDetails productDetails = productReservation.getProductDetails();

                    double productValue = productReservation.getProductDetails().getPrice();

                    reservationDto.setQuantity(updateQuantity);
                    reservationDto.setId(productReservation.getReservationId());

                    reservationDto = cartHelper.updateReservation(reservationDto);

                    ReservationStatus reservationStatus = reservationDto.getStatus();

                    if(updateQuantity == 0){

                        productDetails.setStatus(ReservationStatus.CANCELED);
                        productDetails.setQuantity(updateQuantity);
                        productDetails.setTotal(0.0);

                    }else{

                        productDetails.setStatus(reservationStatus);

                    }

                    if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                        productDetails.setQuantity(updateQuantity);
                        productDetails.setTotal(calculateProductValue(updateQuantity, productValue));

                    }

                    productReservation.setProductDetails(productDetails);

                }

            }

        }

        cart.setProductReservation(reservedProductsList);
        cart.setProductsQuantity(calculateProductsQuantity(reservedProductsList));
        cart.setCartValue(calculateCartValue(reservedProductsList));
        cart.setUpdateDate(LocalDateTime.now());

        return cart;
    }

    public List<String> reservationIds(Cart cart) {

        List<String> reservationIds = new ArrayList<>();

        List<ProductReservation> productReservationList = cart.getProductReservation();

        for (ProductReservation productReservation : productReservationList) {

            reservationIds.add(productReservation.getReservationId());

        }

        return reservationIds;

    }

    public Cart confirm(Mono<Cart> optional) throws ValidationsException {

        Cart cart;

        if(optional.blockOptional().isPresent()){

            cart = optional.blockOptional().get();

        }else{

            throw new ValidationsException("0001");

        }

        if(StringUtils.equals(cart.getCartStatus().toString(), CartStatus.CREATED.toString())){

            cart.setCartStatus(CartStatus.FINISHED);

        }else{

            throw new ValidationsException("0002");

        }


        return cart;

    }

    public Cart cancel(Mono<Cart> optional) throws ValidationsException {

        Cart cart;

        if(optional.blockOptional().isPresent()){

            cart = optional.blockOptional().get();

        }else{

            throw new ValidationsException("0001");

        }

        if(StringUtils.equals(cart.getCartStatus().toString(), CartStatus.CREATED.toString())){

            cart.setCartStatus(CartStatus.CANCELLED);

        }else{

            throw new ValidationsException("0003");

        }

        return cart;

    }

    private double calculateProductValue(int quantity, double value) {

        return quantity * value;

    }

    private int calculateProductsQuantity(List<ProductReservation> productReservationList) {

        int quantity = 0;

        for (ProductReservation productReservation : productReservationList) {

            quantity += productReservation.getProductDetails().getQuantity();

        }

        return quantity;

    }

    private double calculateCartValue(List<ProductReservation> productReservationList) {

        double value = 0;

        for (ProductReservation productReservation : productReservationList) {

            value += productReservation.getProductDetails().getTotal();

        }

        return value;

    }

}
