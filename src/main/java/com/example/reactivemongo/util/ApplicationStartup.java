package com.example.reactivemongo.util;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.service.LogService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final MongoOperations mongoOperations;
    private final LogService service;

    public ApplicationStartup(MongoOperations mongoOperations, LogService service) {
        this.mongoOperations = mongoOperations;
        this.service = service;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            mongoOperations.createCollection(Log.class, CollectionOptions.empty().capped().size(102400).maxDocuments(10));
        }catch (UncategorizedMongoDbException ex) {
            service.save(new Log()
                    .service(ApplicationStartup.class.getTypeName())
                    .message("Collection already exist").level(Level.ERROR)
                    .date(LocalDateTime.now())).subscribe();
        }
    }
}
