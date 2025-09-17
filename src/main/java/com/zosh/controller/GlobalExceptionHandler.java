package com.zosh.controller;

import com.zosh.exception.ProductException;
import com.zosh.exception.UserException; // Assuming you have a UserException
import com.zosh.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // This handler specifically catches ProductException
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ApiResponse> handleProductException(ProductException ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setStatus(false);
        // We return 409 Conflict because the request can't be completed due to the item being in use
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // EXPLANATION for "// You can add other exception handlers here"
    // This class is a central place to handle ALL types of errors.
    // For example, you could add another handler for a UserException.
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> handleUserException(UserException ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setStatus(false);
        // For a user not found, 404 is a good status
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // This is a "catch-all" handler for any other unexpected errors.
    // It prevents ugly stack traces from being sent to the client.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage("An unexpected error occurred: " + ex.getMessage());
        response.setStatus(false);
        // 500 Internal Server Error is the standard for unexpected issues.
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}