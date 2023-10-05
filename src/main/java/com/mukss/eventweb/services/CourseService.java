package com.mukss.eventweb.services;

import java.util.Optional;

import com.mukss.eventweb.entities.Course;

public interface CourseService {

public long count();
	
	public Iterable<Course> findAll();
	
	public Optional<Course> findById(long id);
	
	public Course save(Course event);
	
	public void deleteById(long id);
	
	public void deleteAll();
	
}

