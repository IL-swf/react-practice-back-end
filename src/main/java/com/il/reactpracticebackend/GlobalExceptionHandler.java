package com.il.reactpracticebackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDefaultException (
            Exception ex, WebRequest request, String message)
    {
        return ApiResponse.badRequest();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException (
            Exception ex, WebRequest request, String message)
    {

        return ApiResponse.badRequest();
    }
}
