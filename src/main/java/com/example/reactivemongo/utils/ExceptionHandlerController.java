package com.example.reactivemongo.utils;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.service.LogService;
import com.mongodb.MongoQueryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandlerController {

    private final LogService service;

    public ExceptionHandlerController(LogService service) {
        this.service = service;
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        service.save(new Log()
                .service(ExceptionHandlerController.class.getTypeName())
                .message(ex.getMessage()).level(Level.ERROR)
                .date(LocalDateTime.now()))
                .subscribe();
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
