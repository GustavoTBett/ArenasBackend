package com.projetoWeb.Arenas.service.exception;

public class RefreshTokenExpiredExpection extends RuntimeException {
    public RefreshTokenExpiredExpection(String message) {
        super(message);
    }
}
