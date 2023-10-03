package com.mukss.eventweb.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="course_comments")
public class CourseComment {
	
	@Id
	@Column(name = "course_comment_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime lastEdited;
	
	@NotEmpty
	@Column(columnDefinition = "TEXT")
	private String comment;
	
	private String contact;
	
	@NotEmpty
	private String semester;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="course_id", nullable=false)
	private Course course;
	
	@OneToOne(fetch=FetchType.LAZY)
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
