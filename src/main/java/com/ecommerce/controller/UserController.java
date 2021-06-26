package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.exception.InvalidUserDetailsException;
import com.ecommerce.exception.UserExistsException;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.repository.CartRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    public static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.debug("Class=UserController Method=findById Message=UserId " + id);

		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		log.debug("Class=UserController Method=findByUserName Message=Username " + username);

		User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			log.error("Class=UserController Method=createUser Message=Password length should be greater than 7 and " +
					"match the Confirm Password for "+createUserRequest.getUsername());

			throw new InvalidUserDetailsException();
		}

		if(userRepository.findByUsername(createUserRequest.getUsername()).isPresent()) {
			log.debug("Class=UserController Method=createUser Message=user"+ createUserRequest.getUsername() + " exist already!!!");
			throw new UserExistsException();
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		Cart cart = new Cart();
		user.setCart(cartRepository.save(cart));
		userRepository.save(user);

		log.debug("Class=UserController Method=createUser Message=user "+ user.getUsername() + " created successfully!!!");
		return ResponseEntity.ok(user);
	}

}