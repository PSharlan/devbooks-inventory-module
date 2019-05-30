package com.itechart.devbooks.entity;

/**
 * Represents a Order payment status.
 *
 * @see Order
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
public enum PaymentStatus {
    NONE(0), BILLED(1), PAID(2), CANCELED(10);

    private long id;

    PaymentStatus(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public PaymentStatus getStatusById(int id){
        switch (id){
            case 0: return NONE;
            case 1: return BILLED;
            case 2: return PAID;
            case 10: return CANCELED;
            default: return null;
        }
    }
}