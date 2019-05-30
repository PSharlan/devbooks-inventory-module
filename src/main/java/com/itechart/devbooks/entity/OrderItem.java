package com.itechart.devbooks.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an Order, providing access to the items id, order id, offer id,
 * name, description, category and price.
 * Order item have to be associated with Order.
 *
 * @see Order
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="order_item")
public class OrderItem extends BaseEntity{

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    @Column(name="offer_id")
    private long offerId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="category")
    private String category;

    @Column(name="price")
    private double price;


    /**
     * Constructs a new Order item without association with an Order.
     * Binding Orders and their Items implements by Offer side.
     *
     * @param offerId
     * @param name
     * @param description
     * @param category
     * @param price
     */
    public OrderItem(int offerId, String name, String description, String category, double price) {
        this.offerId = offerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return offerId == orderItem.offerId&&
                Double.compare(orderItem.price, price) == 0 &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(name, orderItem.name) &&
                Objects.equals(description, orderItem.description) &&
                Objects.equals(category, orderItem.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, offerId, name, description, category ,price);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "offerId=" + offerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}
