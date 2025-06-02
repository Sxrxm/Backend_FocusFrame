package com.example.security.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public EntityNotFoundException(String messageKey, Object... args) {
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}