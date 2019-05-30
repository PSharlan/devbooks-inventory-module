package com.itechart.devbooks.entity;

/**
 * Represents an Order status.
 *
 * @see Order
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
public enum OrderStatus {
    NEW(0), PENDING(1), DELIVERY(2), CLOSED(3), CANCELED(10);

    private long id;

    OrderStatus(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public OrderStatus getStatusById(int id){
        switch (id){
            case 0: return NEW;
            case 1: return PENDING;
            case 2: return DELIVERY;
            case 3: return CLOSED;
            case 10: return CANCELED;
            default: return null;

        }
    }



}
