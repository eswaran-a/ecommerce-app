package com.ecommerce.controllers;

import com.ecommerce.TestUtils;
import com.ecommerce.controller.ItemController;
import com.ecommerce.entity.Item;
import com.ecommerce.exception.ItemNotFoundException;
import com.ecommerce.repository.ItemRepository;
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

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void initialise() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        when(itemRepository.findAll()).thenReturn(itemList);

        final ResponseEntity<List<Item>> allItemsRsp = itemController.getItems();
        assertNotNull(allItemsRsp);
        assertEquals(200, allItemsRsp.getStatusCodeValue());
        List<Item> items = allItemsRsp.getBody();
        assertNotNull(items);
        assertArrayEquals(items.toArray(), itemList.toArray());
    }

    @Test
    public void getItemById(){
        Item expected = new Item("Round Widget", new BigDecimal(2.99), "A widget that is round");
        when(itemRepository.findById(1l)).thenReturn(Optional.of(expected));

        final ResponseEntity<Item> itemRsp = itemController.getItemById(1l);
        assertNotNull(itemRsp);
        assertEquals(200, itemRsp.getStatusCodeValue());
        Item actualItem = itemRsp.getBody();
        assertNotNull(actualItem);
        assertEquals("Round Widget", actualItem.getName());
        assertEquals("A widget that is round", actualItem.getDescription());
    }

    @Test
    public void getItemByName(){

        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
        when(itemRepository.findByName("Round Widget")).thenReturn(itemList);

        final ResponseEntity<List<Item>> itemRsp = itemController.getItemsByName("Round Widget");
        assertNotNull(itemRsp);
        assertEquals(200, itemRsp.getStatusCodeValue());
        List<Item> items = itemRsp.getBody();
        assertNotNull(items);
        assertArrayEquals(items.toArray(), itemList.toArray());
    }

    @Test
    public void throw_ItemNotFound_forFindById(){
        when(itemRepository.findById(1l)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            itemController.getItemById(1l);
        });
    }

    @Test
    public void throw_ItemNotFound_forFindByName(){
        when(itemRepository.findByName("Round Widget")).thenReturn(Lists.newArrayList());

        assertThrows(ItemNotFoundException.class, () -> {
            itemController.getItemsByName("Round Widget");
        });
    }
}