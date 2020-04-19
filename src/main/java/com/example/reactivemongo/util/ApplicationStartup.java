package com.example.reactivemongo.util;

import com.example.reactivemongo.entity.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final MongoOperations mongoOperations;

    public ApplicationStartup(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!mongoOperations.collectionExists(Log.class)) {
            mongoOperations.createCollection(Log.class, CollectionOptions.empty()
                    .capped().size(1024 * 1024).maxDocuments(100));
        }
    }
}
