package com.itechart.devbooks.service;

import com.itechart.devbooks.dao.OrderDao;
import com.itechart.devbooks.entity.Order;
import com.itechart.devbooks.entity.OrderItem;
import com.itechart.devbooks.entity.OrderStatus;
import com.itechart.devbooks.entity.PaymentStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class.getName());

    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @Override
    public Order save(Order order) {
        LOGGER.info("Saving order: " + order);
        LOGGER.info("Order items: " + order.getItems());
        order.setCreation(new Timestamp(new Date().getTime()));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.BILLED);
        order.addOrderItems(order.getItems());
        LOGGER.info("Order items: " + order.getItems());
        LOGGER.info("Calculated order: " + order);
        Order savedOrder = orderDao.save(order);
        return order;
    }

    @Override
    public Order update(Order order) {
        LOGGER.info("Updating order: " + order);
        if(findById(order.getId()) == null) {
            LOGGER.info("Offer not fond");
            return null;
        }
        order.addOrderItems(order.getItems());
        LOGGER.info("Calculated order: " + order);
        Order savedOrder = orderDao.save(order);
        return order;
    }

    @Override
    public Order updateStatus(long id, String status) {
        LOGGER.info("Updating order status. Order id: " + id + " | New status: " + status);
        Order order = findById(id);
        if (order == null) return null;
        String st = status.toLowerCase();
        if(st.equals("paid")){
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.DELIVERY);
        } else if (st.equals("canceled")){
            order.setPaymentStatus(PaymentStatus.CANCELED);
            order.setOrderStatus(OrderStatus.CANCELED);
            order.setClosing(new Timestamp(new Date().getTime()));
        } else if (st.equals("closed")){
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.CLOSED);
            order.setClosing(new Timestamp(new Date().getTime()));
        } else return null;
        order = update(order);
        LOGGER.info("Status updated. Payment status: " + order.getPaymentStatus() + " | Order status: " + order.getOrderStatus());
        return order;
    }

    @Override
    public List<Order> findAll() {
        LOGGER.info("Search for all orders");
        List<Order> foundOrders = orderDao.findAll();
        LOGGER.info("Found orders: " + foundOrders);
        return foundOrders;
    }

    @Override
    public Order findById(long id) {
        LOGGER.info("Searching for an order by id: " + id);
        Order order = orderDao.findById(id).orElse(null);
        if(order != null){
            order.getItems().size();
        }
        LOGGER.info("Found order: " + order);
        return order;
    }

    @Override
    public List<Order> findByCustomerId(long customerId) {
        LOGGER.info("Searching for orders by customer id: " + customerId);
        List<Order> foundOrders = orderDao.findByCustomerId(customerId);
        LOGGER.info("Found orders: " + foundOrders);
        return foundOrders;
    }

    @Override
    public List<Order> findOrdersByCategory(String category) {
        LOGGER.info("Finding orders by category: " + category);
        List<Order> allOrders = orderDao.findAll();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order: allOrders) {
            for (OrderItem item : order.getItems()) {
                if (item.getCategory().equals(category)){
                    filteredOrders.add(order);
                    break;
                }
            }
        }
        System.out.println("Found orders: " + filteredOrders);
        return filteredOrders;
    }

    @Override
    public List<Order> findCustomerOrdersByCategory(long customerId, String category) {
        LOGGER.info("Finding orders by category: " + category + " and customer id: " + customerId);
        List<Order> allOrders = findByCustomerId(customerId);
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order: allOrders) {
            for (OrderItem item : order.getItems()) {
                if (item.getCategory().equals(category)){
                    filteredOrders.add(order);
                    break;
                }
            }
        }
        System.out.println("Found orders: " + filteredOrders);
        return filteredOrders;
    }

    @Override
    public void delete(Order order) {
        LOGGER.info("Deleting order: " + order);
        orderDao.delete(order);
    }

    @Override
    public void delete(long id) {
        LOGGER.info("Deleting order by id: " + id);
        orderDao.deleteById(id);
    }
}
