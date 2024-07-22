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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CartBusiness {

    @Resource
    private CartHelper cartHelper;

    public Cart addProductToCart(Mono<Cart> optional, String customerId, ReservationListDto reservationDto) throws ValidationsException, IOException {
        if (optional.blockOptional().isPresent()) {
            return addProduct(optional.blockOptional().get(), reservationDto);
        }

        return createCart(customerId, reservationDto);
    }

    private ProductDetails createProductDetails(ReservationDto reservation) {
        ProductDetails product = new ProductDetails();

        ProductDto productDto = cartHelper.getProduct(reservation.getSku());

        int quantity = reservation.getQuantity();
        double productValue = productDto.getValue();
        ReservationStatus reservationStatus = reservation.getStatus();

        product.setSku(productDto.getCategoryInformationDto().getAvailabilityDto().getSku());
        product.setName(productDto.getCategoryInformationDto().getName());
        product.setPrice(productValue);
        product.setQuantity(quantity);
        product.setStatus(reservationStatus);
        product.setTotal(calculateProductValue(quantity, productValue, reservationStatus));

        return product;
    }

    private ProductReservation createReservationDetails(String reservationId, ProductDetails productDetails) {
        return new ProductReservation(reservationId, productDetails);
    }

    public Cart createCart(String customerId, ReservationListDto reservationListDto) throws ValidationsException, IOException {
        Cart cart = new Cart();

        List<ProductReservation> productReservationList = new ArrayList<>();
        reservationListDto = cartHelper.createReservation(reservationListDto);

        for (ReservationDto reservationDto : reservationListDto.getReservations()) {
            ProductDetails productDetails = createProductDetails(reservationDto);
            ProductReservation productReservation = createReservationDetails(reservationDto.getId(), productDetails);

            productReservationList.add(productReservation);
        }

        Optional<ProductReservation> optional = productReservationList.stream()
                .filter(reservation -> reservation.getProductDetails().getStatus().equals(ReservationStatus.READY))
                .findAny();

        if (optional.isEmpty()) {
            throw new ValidationsException("0100");
        }

        cart.setProductReservation(productReservationList);
        cart.setCustomerId(customerId);
        cart.setProductsQuantity(calculateProductsQuantity(productReservationList));
        cart.setCartValue(calculateCartValue(productReservationList));
        cart.setCreationDate(LocalDateTime.now());
        cart.setUpdateDate(LocalDateTime.now());
        cart.setCartStatus(CartStatus.CREATED);

        return cart;
    }

    public Cart addProduct(Cart cart, ReservationListDto reservationListDto) throws ValidationsException, IOException {
        List<ReservationDto> reservationsDto = reservationListDto.getReservations();
        List<ProductReservation> reservations = cart.getProductReservation();

        if (!CartStatus.CREATED.equals(cart.getCartStatus())) {
            throw new ValidationsException("0004");
        }

        // TODO estou validando daqui para baixo

        List<ReservationDto> reservationsToMake = new ArrayList<>();
        boolean makeReservation = false;

        for (ProductReservation productReservation : reservations) {

            String cartSku = productReservation.getProductDetails().getSku();

            for (ReservationDto reservationDto : reservationsDto) {

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

                    productDetails.setTotal(calculateProductValue(quantity, productValue, reservationStatus));

                    if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                        productDetails.setQuantity(quantity);
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
                productDetails.setTotal(calculateProductValue(quantity, productValue, reservationStatus));

                if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.READY.toString())) {

                    productReservation.setReservationId(reservationDto.getId());


                } else if (StringUtils.equals(reservationStatus.toString(), ReservationStatus.STOCKOUT.toString())) {

                    productDetails.setTotal(0.0);

                }

                productReservation.setProductDetails(productDetails);
                reservations.add(productReservation);

            }

        }

        cart.setProductReservation(reservations);
        cart.setProductsQuantity(calculateProductsQuantity(reservations));
        cart.setCartValue(calculateCartValue(reservations));
        cart.setUpdateDate(LocalDateTime.now());

        return cart;

    }

    public Cart updateCart(CartDto cartDto, Mono<Cart> optional) throws ValidationsException, IOException {
        if (optional.blockOptional().isEmpty()) {
            throw new ValidationsException("0001");
        }

        Cart cart = optional.blockOptional().get();

        if (!StringUtils.equals(cart.getCartStatus().toString(), CartStatus.CREATED.toString())) {
            throw new ValidationsException("0004");
        }

        List<ProductReservation> reservations = updateReservationDetails(cartDto.getProductReservation(), cart.getProductReservation());

        cart.setProductReservation(reservations);
        cart.setProductsQuantity(calculateProductsQuantity(cart.getProductReservation()));
        cart.setCartValue(calculateCartValue(cart.getProductReservation()));
        cart.setUpdateDate(LocalDateTime.now());

        return cart;
    }

    private List<ProductReservation> updateReservationDetails(List<ProductReservationDto> reservationsDtos, List<ProductReservation> reservations) throws IOException {
        for (ProductReservationDto reservationsDto : reservationsDtos) {
            for (ProductReservation reservation : reservations) {
                boolean isTheSameReservation = reservation.getProductDetails().getSku().equals(reservationsDto.getProductDetails().getSku());
                boolean isDifferentQuantity = reservationsDto.getProductDetails().getQuantity() != reservation.getProductDetails().getQuantity();

                if (isTheSameReservation && isDifferentQuantity) {
                    ReservationDto newReservation = cartHelper.updateReservation(
                            new ReservationDto(reservation.getReservationId(),
                                    reservation.getProductDetails().getSku(),
                                    reservationsDto.getProductDetails().getQuantity(),
                                    null)
                    );

                    reservation.getProductDetails().setTotal(
                            calculateProductValue(
                                    newReservation.getQuantity(),
                                    reservation.getProductDetails().getPrice(),
                                    newReservation.getStatus()
                            )
                    );

                    reservation.getProductDetails().setQuantity(ReservationStatus.READY.equals(newReservation.getStatus()) ? newReservation.getQuantity() : 0);
                    reservation.getProductDetails().setStatus(newReservation.getStatus());
                }
            }
        }

        return reservations;
    }

    public List<String> reservationIds(Cart cart) {
        return cart.getProductReservation().stream()
                .map(ProductReservation::getReservationId)
                .collect(Collectors.toList());
    }

    public Cart confirm(Mono<Cart> optional) throws ValidationsException {
        if (optional.blockOptional().isEmpty()) {
            throw new ValidationsException("0001");
        }

        Cart cart = optional.blockOptional().get();

        if (!CartStatus.CREATED.equals(cart.getCartStatus())) {
            throw new ValidationsException("0002");
        }

        cart.setCartStatus(CartStatus.FINISHED);

        return cart;
    }

    public Cart cancel(Mono<Cart> optional) throws ValidationsException {
        if (optional.blockOptional().isEmpty()) {
            throw new ValidationsException("0001");
        }

        Cart cart = optional.blockOptional().get();

        if (!CartStatus.CREATED.equals(cart.getCartStatus())) {
            throw new ValidationsException("0003");
        }

        cart.setCartStatus(CartStatus.CANCELLED);

        return cart;
    }

    private double calculateProductValue(int quantity, double value, ReservationStatus status) {
        return ReservationStatus.READY.equals(status) ? quantity * value : 0;
    }

    private int calculateProductsQuantity(List<ProductReservation> productReservationList) {
        return productReservationList.stream()
                .filter(pr -> pr.getProductDetails().getStatus().equals(ReservationStatus.READY))
                .mapToInt(pr -> pr.getProductDetails().getQuantity())
                .sum();
    }

    private double calculateCartValue(List<ProductReservation> productReservationList) {
        return productReservationList.stream()
                .filter(reservation -> ReservationStatus.READY.equals(reservation.getProductDetails().getStatus()))
                .mapToDouble(reservation -> reservation.getProductDetails().getTotal())
                .sum();
    }

    public void updateReservationsCart(Cart cart, ReservationListDto reservations) {
        Map<String, ReservationDto> reservationMap = reservations.getReservations().stream()
                .collect(Collectors.toMap(ReservationDto::getId, reservation -> reservation));

        cart.getProductReservation().forEach(productReservation -> {
            ReservationDto matchingReservation = reservationMap.get(productReservation.getReservationId());
            if (matchingReservation != null) {
                productReservation.getProductDetails().setStatus(matchingReservation.getStatus());
            }
        });
    }
}
