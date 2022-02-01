package com.kc.springrestapi.utils;

public class RecipeAlreadyExistsException extends RuntimeException{
    public RecipeAlreadyExistsException(String message) {
        super(message);
    }
}
