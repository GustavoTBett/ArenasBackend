package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.service.exception.AlreadyExistsEmailUserException;
import com.projetoWeb.Arenas.service.exception.RefreshTokenExpiredExpection;
import com.projetoWeb.Arenas.service.exception.RefreshTokenNotExistsException;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserNotExistsException(EntityNotExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Entity Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(AlreadyExistsEmailUserException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyExistsEmailUserException(AlreadyExistsEmailUserException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Email Already in Use");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(RefreshTokenNotExistsException.class)
    public ResponseEntity<ProblemDetail> handleRefreshTokenNotExistsException(RefreshTokenNotExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Refresh token not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(RefreshTokenExpiredExpection.class)
    public ResponseEntity<ProblemDetail> handleRefreshTokenExpiredExpection(RefreshTokenExpiredExpection e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Refresh token expired");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}