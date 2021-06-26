package com.ecommerce.controllers;

import com.ecommerce.TestUtils;
import com.ecommerce.controller.CartController;
import com.ecommerce.controller.ItemController;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Item;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ItemNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.ModifyCartRequest;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void initialise() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {

        ModifyCartRequest cartRequest = new ModifyCartRequest("user1", 1, 1);

        List<Item> items = new ArrayList<>();
        items.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        User user = new User(1l,"user1");

        Cart cart = new Cart();
        cart.setTotal(BigDecimal.valueOf(3.98));
        cart.setItems(items);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(items.get(0)));

        final ResponseEntity<Cart> cartRsp = cartController.addTocart(cartRequest);
        assertNotNull(cartRsp);
        assertEquals(200, cartRsp.getStatusCodeValue());
        Cart addedItem = cartRsp.getBody();
        assertNotNull(addedItem);
        assertArrayEquals(cart.getItems().toArray(), addedItem.getItems().toArray());
    }

    @Test
    public void removeFromCart() {

        ModifyCartRequest cartRequest = new ModifyCartRequest("user1", 1, 1);

        List<Item> items = new ArrayList<>();
        items.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        User user = new User(1l,"user1");

        Cart cart = new Cart();
        cart.setTotal(BigDecimal.valueOf(3.98));
        cart.setItems(items);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(itemRepository.findById(1l)).thenReturn(Optional.of(items.get(0)));

        final ResponseEntity<Cart> cartRsp = cartController.removeFromcart(cartRequest);
        assertNotNull(cartRsp);
        assertEquals(200, cartRsp.getStatusCodeValue());
        Cart addedItem = cartRsp.getBody();
        assertNotNull(addedItem);
        assertArrayEquals(cart.getItems().toArray(), addedItem.getItems().toArray());
    }
}
