package com.mukss.eventweb.services;

import java.util.List;
import java.util.Optional;

import com.mukss.eventweb.entities.User;

public interface UserService {
	
	public long count();
	
	public Iterable<User> findAll();
	
	public Optional<User> findById(long id);
	
	public Optional<User> findByName(String userName);
	
	public User save(User user);
	
	public void updateUser(User user, long id);
	
	public List<User> findBymembership(String membership);
	
	public void deleteById(long id);
}
