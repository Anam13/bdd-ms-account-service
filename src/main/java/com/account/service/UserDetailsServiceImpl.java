package com.account.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private String encryptPassword(String plainPassword) {
		return new BCryptPasswordEncoder().encode(plainPassword);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username.equals("user")) {
			return new User("user", encryptPassword("password"), new ArrayList<>());
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}

}