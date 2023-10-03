package com.mukss.eventweb.services;

import java.util.Optional;

import com.mukss.eventweb.entities.CourseComment;

public interface CourseCommentService {
    public long count();

    public Iterable<CourseComment> findAll();

    public Optional<CourseComment> findById(long id);

    public CourseComment save(CourseComment coursecomment);

    public void deleteById(long id);

    public void deleteAll();
}
