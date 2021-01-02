package com.shopdb.ecocitycraft.global.exceptions;

import com.shopdb.ecocitycraft.security.models.exceptions.AuthenticationException;
import com.shopdb.ecocitycraft.security.models.exceptions.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({AuthenticationException.class, AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthenticationException(Exception ex) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleResourceNotFoundException(Exception ex) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception ex) {
        return getExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionResponse getExceptionResponse(String message, HttpStatus status) {
        return new ExceptionResponse(new Timestamp(System.currentTimeMillis()), status.value(), status.getReasonPhrase(), message);
    }
}
