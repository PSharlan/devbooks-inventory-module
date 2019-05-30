package com.itechart.devbooks.model;

public enum PaymentStatusDto {
    NONE(0), BILLED(1), PAID(2), CANCELED(10);

    private long id;

    PaymentStatusDto(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
