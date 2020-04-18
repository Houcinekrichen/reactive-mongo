package com.example.reactivemongo.repository;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LogRepository extends ReactiveMongoRepository<Log,String> {


    @Tailable
    Flux<Log> findByLevel(Level level);

    @Tailable
    Flux<Log> findByLevel(Level level, Pageable pageable);

    @Tailable
    @Query("{}")
    Flux<Log> findAllLogs();
}
