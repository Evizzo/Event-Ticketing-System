package com.eventticketingsystem.eventticketingsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}