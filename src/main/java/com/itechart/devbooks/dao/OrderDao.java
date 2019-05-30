package com.itechart.devbooks.dao;

import com.itechart.devbooks.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(long customerId);
}
