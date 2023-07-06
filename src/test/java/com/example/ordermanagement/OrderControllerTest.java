package com.example.ordermanagement;

import com.example.ordermanagement.controller.OrderController;
import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testGetOrderById() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock repository behavior
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Perform GET request to fetch order by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("123 Main St"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the repository behavior,
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L); // Assign a unique ID to the saved order
            return savedOrder;
        });

        // Perform POST request to create a new order
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"John Doe\", \"shippingAddress\": \"123 Main St\", \"total\": 100.0}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


    @Test
    public void testUpdateOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock repository behavior
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Perform PUT request to update the order
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Jane Smith\", \"shippingAddress\": \"456 Elm St\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock repository behavior
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Perform DELETE request to delete the order
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateOrder_WithBlankCustomerName_ShouldReturnBadRequest() {
        // Create a mock OrderRepository and OrderController
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderController orderController = new OrderController(orderRepository);

        // Prepare an order with a blank customer name
        Order order = new Order();
        order.setCustomerName("");
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Create a mock BindingResult with validation errors
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("order", "customerName", "Customer name is required")
        ));

        // Call the createOrder method and assert the response
        ResponseEntity<?> responseEntity = orderController.createOrder(order, bindingResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("Customer name is required"), responseEntity.getBody());
    }

    @Test
    public void testCreateOrder_WithBlankShippingAddress_ShouldReturnBadRequest() {
        // Similar to the previous test method, but with a blank shipping address
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderController orderController = new OrderController(orderRepository);

        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setShippingAddress("");
        order.setTotal(100.0);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("order", "shippingAddress", "Shipping address is required")
        ));

        ResponseEntity<?> responseEntity = orderController.createOrder(order, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("Shipping address is required"), responseEntity.getBody());
    }

    @Test
    public void testCreateOrder_WithNegativeTotal_ShouldReturnBadRequest() {
        // Similar to the previous test method, but with a negative total
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderController orderController = new OrderController(orderRepository);

        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setShippingAddress("123 Main St");
        order.setTotal(-100.0);

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(
                new FieldError("order", "total", "Total must be a positive value")
        ));

        ResponseEntity<?> responseEntity = orderController.createOrder(order, bindingResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(Collections.singletonList("Total must be a positive value"), responseEntity.getBody());
    }

    @Test
    public void testUpdateOrder_NonExistingOrderId_ShouldReturnNotFound() {
        // Simulating a non-existing order ID for update
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderController orderController = new OrderController(orderRepository);

        Long nonExistingOrderId = 999L;

        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Order updatedOrder = new Order();
        updatedOrder.setCustomerName("Jane Smith");
        updatedOrder.setShippingAddress("456 Elm St");
        updatedOrder.setTotal(200.0);

        ResponseEntity<?> responseEntity = orderController.updateOrder(nonExistingOrderId, updatedOrder);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteOrder_NonExistingOrderId_ShouldReturnNotFound() {
        // Simulating a non-existing order ID for deletion
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        OrderController orderController = new OrderController(orderRepository);

        Long nonExistingOrderId = 999L;

        Mockito.when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = orderController.deleteOrder(nonExistingOrderId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}

