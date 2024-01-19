package com.nat.geeklolspring.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoUserInfoFoundException extends RuntimeException{
    public NoUserInfoFoundException(String message) {
        super(message);
    }
}
