package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.entity.Item;
import com.ecommerce.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.repository.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	public static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.debug("ItemController getItems");

		return ResponseEntity.ok(itemService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.debug("ItemController getItemById "+ id);

		return ResponseEntity.of(itemService.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		log.debug("ItemController getItemsByName "+ name);

		List<Item> items = itemService.findByName(name);
		if(items == null || items.isEmpty())
			log.error("ItemController getItemsByName "+ name+ " not found!!!");

		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
	}
	
}
