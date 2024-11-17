package com.arshad.urlshortener.exception;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String message) {
        super(message);
    }
}
