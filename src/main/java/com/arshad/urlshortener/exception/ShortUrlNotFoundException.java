package com.arshad.urlshortener.exception;

public class ShortUrlNotFoundException extends RuntimeException{

    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
