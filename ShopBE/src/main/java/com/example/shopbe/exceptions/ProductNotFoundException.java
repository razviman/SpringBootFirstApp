package com.example.shopbe.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String s) {
        super(s);
    }
}
