package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserOrder;
import com.ecommerce.service.ItemService;
import com.ecommerce.service.UserService;
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
	private ItemService.OrderService orderService;

	@Autowired
	private UserService userService;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.debug("OrderController submitOrder invoked with username " + username);

		User user = userService.findByUsername(username);

		if(user == null) {
			log.error("OrderController submitOrder: username not found");
			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderService.save(order);

		log.debug("OrderController submitOrder saved for username " + username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userService.findByUsername(username);
		if(user == null) {
			log.error("OrderController getOrdersForUser: username not found");

			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderService.findByUser(user));
	}
}
