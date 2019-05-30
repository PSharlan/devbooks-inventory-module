package com.itechart.devbooks;

import com.itechart.devbooks.config.AppConfig;
import com.itechart.devbooks.entity.Order;
import com.itechart.devbooks.entity.OrderItem;
import com.itechart.devbooks.entity.OrderStatus;
import com.itechart.devbooks.entity.PaymentStatus;
import com.itechart.devbooks.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations="classpath:test.properties")
@ContextConfiguration(classes = { AppConfig.class, InventoryApplication.class}, loader = AnnotationConfigContextLoader.class)
@Sql(value = {"/create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    private Order order1;
    private Order order2;
    private List<OrderItem> items1;
    private List<OrderItem> items2;

    @BeforeEach
    public void setUp() {
        items1 = new ArrayList<>();
        items1.add(new OrderItem(5, "firstOffer", "some description", "category1", 111.11));
        items1.add(new OrderItem(2, "secondOffer", "some description", "category2", 222.2));
        items1.add(new OrderItem(15, "thirdOffer", "some description", "category1", 321.3));

        items2 = new ArrayList<>();
        items2.add(new OrderItem(5, "firstOffer", "some description", "category1", 111.11));
        items2.add(new OrderItem(2, "secondOffer", "some description", "category2", 222.2));
        items1.add(new OrderItem(15, "thirdOffer", "some description", "category1", 321.3));

        order1 = new Order(3, PaymentStatus.BILLED, OrderStatus.PENDING);
        order1.addOrderItems(items1);
        order2 = new Order(2, PaymentStatus.CANCELED, OrderStatus.CANCELED);
        order2.addOrderItems(items2);
    }

    @Test
    public void findAllOrders() {
        Order savedOrder1 = orderService.save(order1);
        Order savedOrder2 = orderService.save(order2);
        List<Order> foundOrders = orderService.findAll();

        assertNotNull(foundOrders);
    }

    @Test
    public void findOrderById() {
        Order savedOrder = orderService.save(order1);
        Order foundOrder = orderService.findById(savedOrder.getId());

        assertEquals(savedOrder, foundOrder);
    }

    @Test
    public void updateOrder() {
        Order savedOrder = orderService.save(order1);
        long savedCustomerId = savedOrder.getCustomerId();

        savedOrder.setCustomerId(10);
        Order updatedOrder = orderService.save(savedOrder);
        long updatedCustomerId = updatedOrder.getCustomerId();

        assertNotNull(updatedOrder);
        assertEquals(savedOrder.getId(), updatedOrder.getId());
        assertNotEquals(savedCustomerId, updatedCustomerId);
    }

    @Test
    public void updateOrderItems() {
        Order savedOrder = orderService.save(order1);
        int savedItemsAmount = savedOrder.getItemsCount();
        double savedPriceAmount = savedOrder.getPriceTotal();

        int i = 0;
        for (OrderItem item : items1) {
            item.setCategory("updated category");
            item.setDescription("updated description");
            item.setName("updated name #" + ++i);
            item.setPrice(111.11);
        }
        items1.add(new OrderItem(1, "new item for updated", "some description", "category5", 0.5));

        savedOrder.addOrderItems(items1);

        Order updatedOrder = orderService.save(savedOrder);

        assertNotNull(updatedOrder.getItems());
        assertNotEquals(savedItemsAmount, updatedOrder.getItemsCount());
        assertNotEquals(savedPriceAmount, updatedOrder.getPriceTotal());
    }

    @Test
    public void deleteOrderById() {
        Order savedOrder = orderService.save(order1);

        long id = savedOrder.getId();
        orderService.delete(id);
        Order deletedOrder = orderService.findById(id);

        assertNull(deletedOrder);
    }


}
