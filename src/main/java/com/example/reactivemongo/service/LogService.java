package com.example.reactivemongo.service;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.repository.LogRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LogService {

    private final LogRepository repository;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public Mono<Log> save(Log log){
        return this.repository.save(log);
    }

    public Flux<Log> findWithTailableCursorBy(){
        return this.repository.findWithTailableCursorBy();
    }

    public Flux<Log> findByLevel(Level level){
        return this.repository.findByLevel(level);
    }

    public Flux<Log> findByLevel(Level level, Pageable pageable){
        return this.repository.findByLevel(level, pageable);
    }
}
