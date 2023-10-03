package com.mukss.eventweb.entities;

import java.util.List;

public class CoursesDTO {
	
	private List<CourseComment> commentlist;

	public List<CourseComment> getCommentList() {
		return commentlist;
	}

	public void setCommentList(List<CourseComment> commentlist) {
		this.commentlist = commentlist;
	}

}
