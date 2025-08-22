package com.projetoWeb.Arenas.service.exception;

public class RefreshTokenNotExistsException extends RuntimeException {
    public RefreshTokenNotExistsException(String message) {
        super(message);
    }
}
