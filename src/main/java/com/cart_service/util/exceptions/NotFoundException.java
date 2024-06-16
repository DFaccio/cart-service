package com.cart_service.util.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(Integer id, String entity) {
        super(String.format("%s com id %d não foi encontrado(a)!", entity, id));
    }

    public NotFoundException(String identificador, String entity) {
        super(String.format("%s com identificador %s não foi encontrado(a)!", entity, identificador));
    }
}