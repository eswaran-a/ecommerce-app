package com.ecommerce.service;

import com.ecommerce.entity.User;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();

        if (user == null) {
            log.error("UserDetailsServiceImpl loadUserByUsername username "+username+" not found!!");
            throw new UsernameNotFoundException(username);
        }

        log.debug("UserDetailsServiceImpl loadUserByUsername username "+username+" found!!");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                Collections.emptyList());
    }
}