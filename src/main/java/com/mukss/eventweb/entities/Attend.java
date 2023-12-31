package com.mukss.eventweb.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="attend")
public class Attend {

    @Id
    @Column(name = "attend_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timeUploaded;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastEdited;

    @NotEmpty(message = "Please enter your Bank Name.")
    private String bankName;
    
    private String status;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

    @ManyToOne(fetch=FetchType.EAGER)
    private User user;

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

    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(LocalDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}