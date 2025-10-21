package com.example.shopbe.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String s) {
        super(s);
    }
}
