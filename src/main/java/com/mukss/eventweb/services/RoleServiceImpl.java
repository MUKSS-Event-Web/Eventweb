package com.mukss.eventweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukss.eventweb.entities.Role;
import com.mukss.eventweb.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public long count() {
		return roleRepository.count();
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

}
