package com.itechart.devbooks.service;

import com.itechart.devbooks.entity.Order;

import java.util.List;

public interface OrderService {

    /**
     * Returns a single order by id.
     * Method returns null if order were not found.
     *
     * @param id - order id
     * @return - order
     */
    Order findById(long id);

    /**
     * Returns all orders.
     *
     * @return - list of all orders.
     */
    List<Order> findAll();

    /**
     * Returns a list of found orders by customer id.
     * Method returns null if orders were not found.
     *
     * @param customerId - customer id
     * @return - found orders
     */
    List<Order> findByCustomerId(long customerId);

    /**
     * Returns a list of found orders by category that stored in order items.
     * Method returns null if orders were not found.
     *
     * @param category - category
     * @return - found orders
     */
    List<Order> findOrdersByCategory(String category);

    /**
     * Returns a list of found orders by customer id and category that stored in order items.
     * Method returns null if orders were not found.
     *
     * @param customerId - customer id
     * @param category - category
     * @return - found orders
     */
    List<Order> findCustomerOrdersByCategory(long customerId, String category);

    /**
     * Saves an order.
     *
     * @param order - order to save
     * @return - saved order
     */
    Order save(Order order);

    /**
     * Updates an order.
     * Method returns null if order were not found to update.
     *
     * @param order - order to update
     * @return - updated order
     */
    Order update(Order order);

    /**
     * Updates an order status.
     * Method returns null if order were not found to update or status do not exist.
     *
     * @param id - order's id
     * @param status - new order status
     * @return - updated order
     */
    Order updateStatus(long id, String status);

    /**
     * Deletes an order.
     *
     * @param order - order to delete
     */
    void delete(Order order);

    /**
     * Deletes an order.
     *
     * @param id - id of the order to delete
     */
    void delete(long id);
}
