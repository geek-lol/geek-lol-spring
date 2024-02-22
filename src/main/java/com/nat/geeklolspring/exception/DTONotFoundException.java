package com.nat.geeklolspring.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DTONotFoundException extends RuntimeException{
    public DTONotFoundException(String message) {
        super(message);
    }
}
