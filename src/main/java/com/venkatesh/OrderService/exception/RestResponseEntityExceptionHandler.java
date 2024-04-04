package com.venkatesh.OrderService.exception;

import com.venkatesh.OrderService.external.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException){
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(customException.getErrorCode())
                .errorMessage(customException.getMessage())
                .build(), HttpStatus.valueOf(customException.getStatus()));
    }
}
