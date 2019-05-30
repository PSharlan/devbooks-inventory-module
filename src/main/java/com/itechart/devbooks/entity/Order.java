package com.itechart.devbooks.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Order, providing access to the orders id, customer id,
 * items count, total price, list of order items, payment and order status, creation and closing time.
 *
 * @see OrderItem
 * @see OrderStatus
 * @see PaymentStatus
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="`order`")
public class Order extends BaseEntity{

    @Column(name="customer_id", nullable = false)
    private long customerId;

    @Column(name= "items_count")
    private int itemsCount;

    @Column(name= "price_total")
    private double priceTotal;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="order_status")
    private OrderStatus orderStatus;

    @Column(name="creation_time")
    private Timestamp creation;

    @Column(name="closing_time")
    private Timestamp closing;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Constructs a new Order without order items.
     * ItemsAmount: 0, PriceAmount: 0.0
     *
     * @param customerId
     * @param paymentStatus
     * @param orderStatus
     */
    public Order(int customerId, PaymentStatus paymentStatus, OrderStatus orderStatus) {
        this.customerId = customerId;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
    }

    /**
     * Constructs a new Order with order items.
     * ItemsAmount and PriceAmount will be calculated.
     *
     * @param customerId
     * @param paymentStatus
     * @param orderStatus
     * @param items
     */
    public Order(int customerId, PaymentStatus paymentStatus, OrderStatus orderStatus, List<OrderItem> items) {
        this(customerId, paymentStatus, orderStatus);
        addOrderItems(items);
    }

    /**
     * Adds item to order and recalculates it.
     *
     * @param item - item to add
     */
    public void addOrderItems(OrderItem item){
        item.setOrder(this);
        getItems().add(item);
        countItems();
    }

    /**
     * Adds list of items to order and recalculates it.
     *
     * @param items - item to add
     */
    public void addOrderItems(List<OrderItem> items){
        System.out.println("ITEMS: " + items);
        for (OrderItem item : items) {
            item.setOrder(this);
        }
        setItems(items);
        countItems();
    }

    /**
     * Deletes item from order and recalculates it.
     *
     * @param item - item to add
     */
    public void removeOrderItems(OrderItem item){
        this.items.remove(item);
        countItems();
    }

    /**
     * Deletes list of items from order and recalculates it.
     *
     * @param items - item to add
     */
    public void removeOrderItems(List<OrderItem> items){
        for (OrderItem item : items) {
            removeOrderItems(item);
        }
    }

    private void countItems(){
        int count = 0;
        double price = 0;
        for (OrderItem item : this.items) {
            count++;
            price += item.getPrice();
        }
        this.itemsCount = count;
        DecimalFormat df = new DecimalFormat("#.##");
        this.priceTotal = Double.valueOf(df.format(price));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return customerId == order.customerId &&
                itemsCount == order.itemsCount &&
                Double.compare(order.priceTotal, priceTotal) == 0 &&
                paymentStatus == order.paymentStatus &&
                orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, itemsCount, priceTotal, paymentStatus, orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerId=" + customerId +
                ", itemsAmount=" + itemsCount +
                ", priceAmount=" + priceTotal +
                ", paymentStatus=" + paymentStatus +
                ", orderStatus=" + orderStatus +
                ", creationTS=" + creation +
                ", closingTS=" + closing +
                '}';
    }
}
