package com.ecommerce.service;

import com.ecommerce.entity.Item;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserOrder;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findByName(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Service
    public static class OrderService {
        @Autowired
        private OrderRepository orderRepository;

        public void save(UserOrder order) {
            orderRepository.save(order);
        }

        public List<UserOrder> findByUser(User user) {
            return orderRepository.findByUser(user);
        }
    }
}
