package com.itechart.devbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    private Long id;

    @Min(value = 1, message = "Customer id must be specified")
    private long customerId;
    private int itemsCount;
    private double priceTotal;
    private PaymentStatusDto paymentStatus;
    private OrderStatusDto orderStatus;
    private Timestamp creation;
    private Timestamp closing;
    private List<OrderItemDto> items = new ArrayList<>();

}
