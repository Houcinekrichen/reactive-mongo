package com.example.reactivemongo.resource;

import com.example.reactivemongo.entity.User;
import com.example.reactivemongo.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users")
public class UserResource {

    private final UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<User> save(@RequestBody User user){
        return this.service.save(user);
    }

    @GetMapping
    public Flux<User> findAll(){
        return this.service.findAll();
    }

    @DeleteMapping("{id}")
    private void delete(@PathVariable("id") String id){
        this.service.deleteById(id);
    }
}
