package com.projetoWeb.Arenas.service.exception;

public class AlreadyExistsEmailUserException extends RuntimeException {
    public AlreadyExistsEmailUserException(String message) {
        super(message);
    }
}
