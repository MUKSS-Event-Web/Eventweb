package com.mukss.eventweb.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukss.eventweb.entities.CourseComment;
import com.mukss.eventweb.repositories.CourseCommentRepository;

@Service
public class CourseCommentServiceImpl implements CourseCommentService {

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Override
    public long count() {
        return courseCommentRepository.count();
    }

    @Override
    public Iterable<CourseComment> findAll() {
        return courseCommentRepository.findAll();
    }

    @Override
    public Optional<CourseComment> findById(long id) {
        return courseCommentRepository.findById(id);
    }

    @Override
    public CourseComment save(CourseComment courseComment) {
        return courseCommentRepository.save(courseComment);
    }

    @Override
    public void deleteById(long id) {
    	courseCommentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
    	courseCommentRepository.deleteAll();
    }
}