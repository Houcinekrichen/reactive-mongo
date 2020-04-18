package com.example.reactivemongo.service;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.entity.User;
import com.example.reactivemongo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;
    private final LogService logService ;

    public UserService(UserRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    public Mono<User> save(User user){
        Assert.isNull(user.getId(),"Id should be null");
        this.logService.save(new Log()
                .service(UserService.class.getTypeName())
                .message("Save new User").level(Level.DEBUG)
                .date(LocalDateTime.now())).subscribe();
        return this.repository.save(user);
    }

    public Flux<User> findAll(){
        this.logService.save(new Log()
                .service(UserService.class.getTypeName())
                .message("Find all users").level(Level.DEBUG)
                .date(LocalDateTime.now())).subscribe();
        return this.repository.findAll();
    }

    public void deleteById(String id) {
        Assert.isTrue(id.isEmpty(),"Id should not be empty");
        this.logService.save(new Log()
                .service(UserService.class.getTypeName())
                .message("Delete user").level(Level.DEBUG)
                .date(LocalDateTime.now())).subscribe();
        this.repository.deleteById(id).subscribe();
    }
}
