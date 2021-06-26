package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Item;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ItemNotFoundException;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ItemRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	public static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername()).orElseThrow(UserNotFoundException::new);
		Item item = itemRepository.findById(request.getItemId()).orElseThrow(ItemNotFoundException::new);

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.addItem(item));
		cartRepository.save(cart);
		log.debug("CartController addTocart Item "+ request.getItemId()+ " added to cart successfully for user "+ request.getUsername());
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		User user = userRepository.findByUsername(request.getUsername()).orElseThrow(UserNotFoundException::new);
		Item item = itemRepository.findById(request.getItemId()).orElseThrow(ItemNotFoundException::new);

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.removeItem(item));
		cartRepository.save(cart);
		log.debug("CartController removeFromcart Item "+ request.getItemId()+ " removed from cart successfully for user "+ request.getUsername());
		return ResponseEntity.ok(cart);
	}
}
