package com.mukss.eventweb.config.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByuserName(username).orElseThrow();
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new CustomUserDetails(user);
	}

}
