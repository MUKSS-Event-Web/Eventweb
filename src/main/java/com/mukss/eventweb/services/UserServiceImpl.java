package com.mukss.eventweb.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukss.eventweb.entities.User;
import com.mukss.eventweb.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void updateUser(User user, long id) {
		User userInDB = userRepository.findById(id).get();
		userInDB.setFirstName(user.getFirstName());
		userInDB.setLastName(user.getLastName());
		userInDB.setUserName(user.getUserName());
		userInDB.setPassword(user.getPassword());
		userInDB.setEmail(user.getEmail());
		userInDB.setEnabled(user.isEnabled());
//		userInDB.setRoles(user.getRoles());
		userRepository.save(userInDB);
	}

	@Override
	public Optional<User> findByName(String userName) {
		return userRepository.findByuserName(userName);
	}

	@Override
	public List<User> findBymembership(String membership) {
		return userRepository.findBymembership(membership);
	}

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}

}
