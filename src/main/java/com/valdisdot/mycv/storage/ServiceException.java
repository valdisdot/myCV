package com.valdisdot.mycv.storage;

import com.valdisdot.mycv.entity.Identifiable;

public class ServiceException extends RuntimeException {
    private Identifiable instance;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Identifiable instance) {
        super(message);
        this.instance = instance;
    }

    public Identifiable getInstance() {
        return instance;
    }
}
