package com.mukss.eventweb.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import javax.persistence.CascadeType;

import javax.persistence.OneToMany;

@Entity
@Table(name="events")
public class Event {
	
	@Id
	@Column(name = "event_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime timeUploaded;
	
	@NotEmpty
	private String title;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	// set as string due to timezones and stuff ig
	private String time;

	
	@NotEmpty
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String location;
	
    private String imageName;

    private String imageFileType;
    
    @Lob
    private byte[] data;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public LocalDateTime getTimeUploaded() {
		return timeUploaded;
	}

	public void setTimeUploaded(LocalDateTime timeUploaded) {
		this.timeUploaded = timeUploaded;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageFileType() {
		return imageFileType;
	}

	public void setImageFileType(String imageFileType) {
		this.imageFileType = imageFileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	// attend code merge
	
	private Integer fee;
	
	@OneToMany(mappedBy="event", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Attend> attends;
	
	public Integer getFee() {
		return fee;
	}
	
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	
	public List<Attend> getAttends(){
		return attends;
	}
	
	public void addAttend(Attend attend) {
		this.attends.add(attend);
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
