package com.example.reactivemongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Log {

    @Id
    private String id;
    private String service;
    private String message;
    private Level level;
    private LocalDateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Log service(String service) {
        this.service = service;
        return this;
    }
    public Log message(String message) {
        this.message = message;
        return this;
    }
    public Log level(Level level) {
        this.level = level;
        return this;
    }
    public Log date(LocalDateTime date) {
        this.date = date;
        return this;
    }
}
