package com.example.ordermanagement;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveOrder() {
        // Create an order object
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Save the order to the repository
        Order savedOrder = orderRepository.save(order);

        // Verify that the order is saved with an ID
        Assertions.assertNotNull(savedOrder.getId());

        // Fetch the order from the repository by ID
        Optional<Order> fetchedOrder = orderRepository.findById(savedOrder.getId());

        // Verify that the fetched order is not null
        Assertions.assertTrue(fetchedOrder.isPresent());

        // Verify that the fetched order matches the original order
        Assertions.assertEquals("John Doe", fetchedOrder.get().getCustomerName());
        Assertions.assertEquals(LocalDate.now(), fetchedOrder.get().getOrderDate());
        Assertions.assertEquals("123 Main St", fetchedOrder.get().getShippingAddress());
        Assertions.assertEquals(100.0, fetchedOrder.get().getTotal());
    }

    @Test
    public void testFindOrderById() {
        // Save an order to the repository
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);
        orderRepository.save(order);

        // Fetch the saved order by ID
        Order fetchedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify that the fetched order is not null
        Assertions.assertNotNull(fetchedOrder);
    }

    @Test
    public void testUpdateOrder() {
        // Save an order to the repository
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);
        orderRepository.save(order);

        // Update the order details
        order.setCustomerName("Jane Smith");
        order.setTotal(200.0);
        orderRepository.save(order);

        // Fetch the updated order by ID
        Order updatedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify that the fetched order is not null
        Assertions.assertNotNull(updatedOrder);
    }

    @Test
    public void testDeleteOrder() {
        // Save an order to the repository
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);
        orderRepository.save(order);

        // Delete the order from the repository
        orderRepository.deleteById(order.getId());

        // Try to fetch the deleted order by ID
        Order deletedOrder = orderRepository.findById(order.getId()).orElse(null);

        // Verify that the fetched order is null
        Assertions.assertNull(deletedOrder);
    }
}
