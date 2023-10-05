package com.mukss.eventweb.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukss.eventweb.entities.Course;
import com.mukss.eventweb.repositories.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService{
	
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public long count() {
		return courseRepository.count();
	}

	@Override
	public Iterable<Course> findAll() {
		return courseRepository.findAll();
	}

	@Override
	public Optional<Course> findById(long id) {
		return courseRepository.findById(id);
	}

	@Override
	public Course save(Course course) {
		return courseRepository.save(course);
	}
	
	
	@Override
	public void deleteById(long id) {
		courseRepository.deleteById(id);
	}
	
	@Override
	public void deleteAll() {
		courseRepository.deleteAll();
	}
}

