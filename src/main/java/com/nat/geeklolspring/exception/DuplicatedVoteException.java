package com.nat.geeklolspring.exception;

public class DuplicatedVoteException extends RuntimeException{
    public DuplicatedVoteException(String message) {
        super(message);
    }
}
