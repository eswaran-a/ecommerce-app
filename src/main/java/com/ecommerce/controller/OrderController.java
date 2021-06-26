package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserOrder;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	public static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.debug("Class=OrderController Method=submitOrder Message=invoked with username " + username);

		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.debug("Class=OrderController Method=submitOrder Message=saved for username " + username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		log.debug("Class=OrderController Method=getOrdersForUser Message=invoked for username " + username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
