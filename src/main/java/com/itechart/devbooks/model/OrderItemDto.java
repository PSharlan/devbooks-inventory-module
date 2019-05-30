package com.itechart.devbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class OrderItemDto {

    private Long id;

    @Min(value = 1, message = "Offer id must be specified")
    private long offerId;

    @NotEmpty(message = "Offer name can not be empty")
    private String name;

    @NotEmpty(message = "Offer description can not be empty")
    private String description;

    @NotEmpty(message = "Offer category can not be empty")
    private String category;

    @Min(value = 1, message = "Offer price must be specified")
    private double price;

}
