package com.mukss.eventweb.entities;

import java.util.List;

public class CoursesDTO {
	
	public List<CourseComment> commentList;

	public List<CourseComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CourseComment> commentList) {
		this.commentList = commentList;
	}

}
