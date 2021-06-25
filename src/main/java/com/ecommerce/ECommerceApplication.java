package com.ecommerce;

import com.ecommerce.controller.CartController;
import com.ecommerce.entity.Item;
import com.ecommerce.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

@EnableJpaRepositories("com.ecommerce.repository")
@EntityScan("com.ecommerce.entity")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ECommerceApplication {
	public static final Logger log = LoggerFactory.getLogger(ECommerceApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ItemRepository repository) {
		log.info("ECommerceApplication Item database initialized");
		return args -> {
			repository.save(new Item("Round Widget", new BigDecimal(2.99), "A widget that is round"));
			repository.save(new Item("Square Widget", new BigDecimal(1.99), "A widget that is square"));
		};
	}
}