package com.kc.springrestapi.utils;

public class RecipeAlreadyExistsException extends Exception{
    public RecipeAlreadyExistsException(String message) {
        super(message);
    }
}
