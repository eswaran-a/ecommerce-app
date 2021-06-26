package com.ecommerce.controllers;

import com.ecommerce.TestUtils;
import com.ecommerce.controller.OrderController;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Item;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserOrder;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void initialise() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void orderHistoryForUser() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        User user = new User(1l,"user1");

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setId(1l);
        userOrder.setTotal(BigDecimal.valueOf(3.98));

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Lists.newArrayList(userOrder));

        final ResponseEntity<List<UserOrder>> orderHistoryRsp = orderController.getOrdersForUser("user1");
        assertNotNull(orderHistoryRsp);
        assertEquals(200, orderHistoryRsp.getStatusCodeValue());
        List<UserOrder> userOrderList = orderHistoryRsp.getBody();
        assertNotNull(userOrderList);
        assertArrayEquals(userOrderList.get(0).getItems().toArray(), items.toArray());
        assertEquals(userOrderList.get(0).getUser(), user);
    }

    @Test
    public void submitOrder() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        User user = new User(1l,"user1");

        Cart cart = new Cart();
        cart.setTotal(BigDecimal.valueOf(3.98));
        cart.setItems(items);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        final ResponseEntity<UserOrder> submitOrderRsp = orderController.submit("user1");
        assertNotNull(submitOrderRsp);
        assertEquals(200, submitOrderRsp.getStatusCodeValue());
        UserOrder userOrder = submitOrderRsp.getBody();
        assertNotNull(userOrder);
        assertArrayEquals(userOrder.getItems().toArray(), items.toArray());
        assertEquals(userOrder.getUser(), user);
    }
}