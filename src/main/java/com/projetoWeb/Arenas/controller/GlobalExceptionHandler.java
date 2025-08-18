package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.service.exception.AlreadyExistsEmailUserException;
import com.projetoWeb.Arenas.service.exception.UserNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserNotExistsException(UserNotExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("User Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(AlreadyExistsEmailUserException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyExistsEmailUserException(AlreadyExistsEmailUserException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("Email Already in Use");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }
}