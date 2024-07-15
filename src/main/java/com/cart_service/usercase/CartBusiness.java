package com.cart_service.usercase;

import com.cart_service.entities.Cart;
import com.cart_service.entities.ProductDetails;
import com.cart_service.entities.ProductReservation;
import com.cart_service.interfaceadapters.helper.CartHelper;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.enums.ReservationStatus;
import com.cart_service.util.exceptions.ValidationsException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CartBusiness {

    @Resource
    private CartHelper cartHelper;

    public Cart addProductToCart(Optional<Cart> optional, String costumertId, ReservationListDto reservationDto) throws ValidationsException {

        Cart cart;

        if (optional.isPresent()) {

            cart = addProduct(optional.get(), reservationDto);

        } else {

            cart = createCart(costumertId, reservationDto);

        }

        return cart;

    }

    public Cart createCart(String costumertId, ReservationListDto reservationListDto) throws ValidationsException {

        Cart cart = new Cart();
        List<ProductReservation> productReservationList = new ArrayList<>();

        reservationListDto = cartHelper.createReservation(reservationListDto);

        boolean hasReservation = false;

        for (ReservationDto reservationDto : reservationListDto.getReservationDto()) {

            ProductDetails productDetails = new ProductDetails();
            ProductReservation productReservation = new ProductReservation();

            ProductDto productDto = cartHelper.getProduct(reservationDto.getSku());

            int quantity = reservationDto.getQuantity();
            double productValue = productDto.getValue();
            String reservationStatus = reservationDto.getStatus();

            productDetails.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
            productDetails.setName(productDto.getCategoryInformationDto().getName());
            productDetails.setPrice(productValue);
            productDetails.setQuantity(quantity);
            productDetails.setStatus(reservationStatus);

            if (StringUtils.equals(reservationStatus, ReservationStatus.READY.toString())) {

                hasReservation = true;

                productReservation.setReservationId(reservationDto.getId());
                productDetails.setTotal(calculateProductValue(quantity, productValue));

            } else if (StringUtils.equals(reservationStatus, ReservationStatus.STOCKOUT.toString())) {

                productDetails.setTotal(0);

            }

            productReservation.setProductDetails(productDetails);
            productReservationList.add(productReservation);

        }

        if (hasReservation) {

            cart.setProductReservation(productReservationList);
            cart.setCostumerId(costumertId);
            cart.setProductsQuantity(calculateProductsQuantity(productReservationList));
            cart.setCartValue(calculateCartValue(productReservationList));
            cart.setCreationDate(LocalDateTime.now());
            cart.setUpdateDate(LocalDateTime.now());
            cart.setStatus(CartStatus.CREATED);

        } else {

            throw new ValidationsException("0100");

        }

        return cart;

    }

    public Cart addProduct(Cart cart, ReservationListDto reservationListDto) throws ValidationsException {

        List<ReservationDto> reservations = reservationListDto.getReservationDto();

        List<ProductReservation> productReservationList = cart.getProductReservation();
        List<ReservationDto> reservationsToMake = new ArrayList<>();

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

                    String reservationStatus = reservationDto.getStatus();

                    productDetails.setStatus(reservationStatus);

                    if (StringUtils.equals(reservationStatus, ReservationStatus.READY.toString())) {

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
            makeReservationList.setReservationDto(reservationsToMake);

            makeReservationList = cartHelper.createReservation(makeReservationList);

            for (ReservationDto reservationDto : makeReservationList.getReservationDto()) {

                ProductDetails productDetails = new ProductDetails();
                ProductReservation productReservation = new ProductReservation();

                ProductDto productDto = cartHelper.getProduct(reservationDto.getSku());

                int quantity = reservationDto.getQuantity();
                double productValue = productDto.getValue();
                String reservationStatus = reservationDto.getStatus();

                productDetails.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
                productDetails.setName(productDto.getCategoryInformationDto().getName());
                productDetails.setPrice(productValue);
                productDetails.setQuantity(quantity);
                productDetails.setStatus(reservationStatus);

                if (StringUtils.equals(reservationStatus, ReservationStatus.READY.toString())) {

                    productReservation.setReservationId(reservationDto.getId());
                    productDetails.setTotal(calculateProductValue(quantity, productValue));

                } else if (StringUtils.equals(reservationStatus, ReservationStatus.STOCKOUT.toString())) {

                    productDetails.setTotal(0);

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

    public List<String> reservationIds(Cart cart) {

        List<String> reservationIds = new ArrayList<>();

        List<ProductReservation> productReservationList = cart.getProductReservation();

        for (ProductReservation productReservation : productReservationList) {

            reservationIds.add(productReservation.getReservationId());

        }

        return reservationIds;

    }

    public Cart confirm(Cart cart) {

        cart.setStatus(CartStatus.FINISHED);

        return cart;

    }

    public Cart cancel(Cart cart) {

        cart.setStatus(CartStatus.CANCELLED);

        return cart;

    }

//    public boolean skuAlreadyInCart(Cart cart, String sku) {
//
//        List<ProductReservation> productReservationList = cart.getProductReservation();
//
//        for (ProductReservation productReservation : productReservationList) {
//
//            if (StringUtils.equals(productReservation.getProductDetails().getSku(), sku)) {
//
//                return true;
//
//            }
//
//        }
//
//        return false;
//
//    }

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
