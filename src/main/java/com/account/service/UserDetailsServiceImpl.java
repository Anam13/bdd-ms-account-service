package com.account.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static List<User> users = new ArrayList<>();
	static {
		users.add(new User("user1", encryptPassword("password1"), new ArrayList<>()));
		users.add(new User("user2", encryptPassword("password2"), new ArrayList<>()));
		users.add(new User("user3", encryptPassword("password3"), new ArrayList<>()));
	}

	private static String encryptPassword(String plainPassword) {
		return new BCryptPasswordEncoder().encode(plainPassword);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username != null) {
			return users.stream().filter(user -> user.getUsername().equals(username))
					.findFirst()
					.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		}
		return null;
	}


}