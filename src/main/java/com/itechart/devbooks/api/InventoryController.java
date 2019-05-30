package com.itechart.devbooks.api;

import com.itechart.devbooks.entity.Order;
import com.itechart.devbooks.exception.EntityNotFoundException;
import com.itechart.devbooks.exception.EntityNotUpdatedException;
import com.itechart.devbooks.model.OrderDto;
import com.itechart.devbooks.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inventory")
@Api(value = "/api/v1/inventory", description = "Manage customers inventory")
public class InventoryController {

    private static final Logger LOGGER = LogManager.getLogger(InventoryController.class.getName());

    OrderService orderService;
    private ModelMapper modelMapper;

    @Autowired
    public InventoryController(OrderService orderService, ModelMapper modelMapper){
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(
            value = "Create order",
            notes = "Required order instance"
    )
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(
            @ApiParam(value = "Order instance", required = true)
            @Valid @RequestBody OrderDto orderDto) {
        Order order = convertToEntity(orderDto);
        LOGGER.info("Saving order: " + order);
        Order savedOrder = orderService.save(order);
        LOGGER.info("Saved order: " + order + " with id: " + order.getId());
        return convertToDto(savedOrder);
    }

    @ApiOperation(value = "Return all existing orders")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrders() {
        LOGGER.info("Search for all orders");
        List<Order> foundOrders = orderService.findAll();
        LOGGER.info("Found orders: " + foundOrders);
        return foundOrders.stream()
                .map(order -> convertToDto(order))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Return list of an orders by customer id")
    @RequestMapping(value = "customers/{customerId}/orders", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAllOrdersByCustomerId(
            @ApiParam(value = "Customer id", required = true)
            @PathVariable long customerId) {
        LOGGER.info("Searching for orders by customer id: " + customerId);
        List<Order> customerOrders = orderService.findByCustomerId(customerId);
        if(customerOrders == null) {
            LOGGER.info("Orders not found");
            throw new EntityNotFoundException(Order.class, customerId);
        }
        LOGGER.info("Found orders: " + customerOrders);
        return customerOrders.stream()
                .map(order -> convertToDto(order))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Return order by id")
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrderById(
            @ApiParam(value = "Id of an order to lookup for", required = true)
            @PathVariable long id) {
        LOGGER.info("Searching for an order by id: " + id);
        Order foundOrder = orderService.findById(id);
        if(foundOrder == null) {
            LOGGER.info("Order not found");
            throw new EntityNotFoundException(Order.class, id);
        }
        LOGGER.info("Found order: " + foundOrder);
        return convertToDto(foundOrder);
    }

    @ApiOperation(
            value = "Return customer orders by category",
            notes = "Required customer id and category name"
    )
    @RequestMapping(value = "customers/{customerId}/orders/categories/{category}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getCustomerOrdersByCategory(
            @ApiParam(value = "Id of a customer to lookup for", required = true)
            @PathVariable long customerId,
            @ApiParam(value = "Category name", required = true)
            @PathVariable String category) {
        LOGGER.info("Searching for orders by category: " + category + " and customer id: " + customerId);
        List<Order> foundOrders = orderService.findCustomerOrdersByCategory(customerId, category);
        LOGGER.info("Found orders: " + foundOrders);
        return foundOrders.stream()
                .map(order -> convertToDto(order))
                .collect(Collectors.toList());
    }

    @ApiOperation(
            value = "Return orders by category",
            notes = "Required category name"
    )
    @RequestMapping(value = "orders/categories/{category}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrdersByCategory(
            @ApiParam(value = "Category name", required = true)
            @PathVariable String category) {
        LOGGER.info("Searching for orders by category: " + category);
        List<Order> foundOrders = orderService.findOrdersByCategory(category);
        LOGGER.info("Found orders: " + foundOrders);
        return foundOrders.stream()
                .map(order -> convertToDto(order))
                .collect(Collectors.toList());
    }
    
    @ApiOperation(
            value = "Update order",
            notes = "Required order instance"
    )
    @RequestMapping(value = "/orders", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updatedOrder(
            @ApiParam(value = "Order instance", required = true)
            @Valid @RequestBody OrderDto orderDto) {
        Order order = convertToEntity(orderDto);
        LOGGER.info("Updating order: " + order);
        Order updatedOrder = orderService.update(order);
        if(updatedOrder == null) {
            LOGGER.info("Can not update not existing order");
            throw new EntityNotUpdatedException(Order.class, order.getId());
        }
        LOGGER.info("Updated order: " + updatedOrder);
        return convertToDto(updatedOrder);
    }

    @ApiOperation(
            value = "Update order status",
            notes = "Required order id and payment status"
    )
    @RequestMapping(value = "/orders/{id}/status", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updatedOrderStatus(
            @ApiParam(value = "Order status", required = true)
            @RequestBody String paymentStatus,
            @ApiParam(value = "Order id", required = true)
            @PathVariable long id) {
        LOGGER.info("Updating order status. Order id: " + id + " | New payment status: " + paymentStatus);
        Order updatedOrder = orderService.updateStatus(id, paymentStatus);
        if(updatedOrder == null) {
            LOGGER.info("Not existing order or status");
            throw new EntityNotUpdatedException(Order.class, id);
        }
        LOGGER.info("Updated order: " + updatedOrder);
        return convertToDto(updatedOrder);
    }

    @ApiOperation(value = "Delete order by id")
    @RequestMapping(value = "/orders/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(
            @ApiParam(value = "Id of an order to delete", required = true)
            @PathVariable long id) {
        LOGGER.info("Deleting order by id: " + id);
        orderService.delete(id);
        LOGGER.info("Order deleted");
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        return orderDto;
    }

    private Order convertToEntity(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        return order;
    }
}
