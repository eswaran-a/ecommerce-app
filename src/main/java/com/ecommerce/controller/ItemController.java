package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.entity.Item;
import com.ecommerce.exception.ItemNotFoundException;
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
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.debug("Class=ItemController Method= getItems");

		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.debug("Class=ItemController Method=getItemById Message=ItemId "+ id);
		Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
		return ResponseEntity.ok(item);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		log.debug("Class=ItemController Method=getItemsByName Message=ItemName "+ name);

		List<Item> items = itemRepository.findByName(name);
		if(items == null || items.isEmpty()){
			log.error("Class=ItemController Method=getItemsByName Message="+ name+ " not found!!!");
			throw new ItemNotFoundException();
		}


		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
	}
	
}
