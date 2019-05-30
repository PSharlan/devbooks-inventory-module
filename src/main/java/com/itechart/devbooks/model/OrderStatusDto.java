package com.itechart.devbooks.model;

public enum OrderStatusDto {
    NEW(0), PENDING(1), DELIVERY(2), CLOSED(3), CANCELED(10);

    private long id;

    OrderStatusDto(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
