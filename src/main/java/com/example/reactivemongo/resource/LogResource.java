package com.example.reactivemongo.resource;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.service.LogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("logs")
public class LogResource {

    private final LogService service;

    public LogResource(LogService service) {
        this.service = service;
    }

    @GetMapping(value = "all/{level}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Log> findByLevel(@PathVariable("level") String level){
        return service.findByLevel(Level.valueOf(level.toUpperCase()));
    }

    @GetMapping(value = "all",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Log> findByLevel(){
        return service.findWithTailableCursorBy();
    }

    @GetMapping(value = "{level}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Log> findByLevel(@PathVariable("level") String level,
                                 @RequestParam(value = "page") int pageIndex,
                                 @RequestParam(value = "size") int pageSize,
                                 @RequestParam(value = "sort" ,required = false) Sort sort){
        return service.findByLevel(Level.valueOf(level.toUpperCase()),
                PageRequest.of(pageIndex, pageSize, sort == null ? Sort.unsorted() : sort));
    }
}
