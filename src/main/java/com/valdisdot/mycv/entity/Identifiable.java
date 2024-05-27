package com.valdisdot.mycv.entity;

public abstract class Identifiable {
    private final Long id;

    protected Identifiable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
