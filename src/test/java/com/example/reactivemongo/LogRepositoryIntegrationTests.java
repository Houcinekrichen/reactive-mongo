package com.example.reactivemongo;

import com.example.reactivemongo.entity.Level;
import com.example.reactivemongo.entity.Log;
import com.example.reactivemongo.repository.LogRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogRepositoryIntegrationTests {

    @Autowired
    private LogRepository repository;
    @Autowired
    private ReactiveMongoOperations operations;

    @Before
    public void setUp() {

        Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(Log.class) //
                .flatMap(exists -> exists ? operations.dropCollection(Log.class) : Mono.just(exists)) //
                .then(operations.createCollection(Log.class, CollectionOptions.empty() //
                        .size(1024 * 1024)
                        .maxDocuments(100)
                        .capped()));

        StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

        Flux<Log> insertAll = operations.insertAll(Arrays.asList(
                new Log().message("Debug").date(LocalDateTime.now()).level(Level.DEBUG),
                new Log().message("Info").date(LocalDateTime.now()).level(Level.INFO),
                new Log().message("Error").date(LocalDateTime.now()).level(Level.ERROR),
                new Log().message("Warn").date(LocalDateTime.now()).level(Level.WARN)));

        StepVerifier.create(insertAll).expectNextCount(4).verifyComplete();
    }

    /**
     * Note that the all object conversions are performed before the results are printed to the console.
     */
    @Test
    public void shouldPerformConversionBeforeResultProcessing() {

        StepVerifier.create(repository.findAll()
                .doOnNext(System.out::println))
                .expectNextCount(4)
                .verifyComplete();
    }

    /**
     * A tailable cursor streams data using {@link Flux} as it arrives inside the capped collection.
     */
    @Test
    public void shouldStreamDataWithTailableCursor() throws Exception {

        Queue<Log> logs = new ConcurrentLinkedQueue<>();

        Disposable disposable = repository.findWithTailableCursorBy() //
                .doOnNext(System.out::println) //
                .doOnNext(logs::add) //
                .doOnComplete(() -> System.out.println("Complete")) //
                .doOnTerminate(() -> System.out.println("Terminated")) //
                .subscribe();

        Thread.sleep(100);

        StepVerifier.create(repository.save(new Log().message("Debug ").date(LocalDateTime.now()).level(Level.INFO))) //
                .expectNextCount(1) //
                .verifyComplete();

        Thread.sleep(100);

        disposable.dispose();

        StepVerifier.create(repository.save(new Log().message("Info ").date(LocalDateTime.now()).level(Level.INFO))) //
                .expectNextCount(1) //
                .verifyComplete();
        Thread.sleep(100);

        assertThat(logs).hasSize(5);
    }

    /**
     * Fetch data using query derivation.
     */
    @Test
    public void shouldQueryDataWithQueryDerivation() {
        StepVerifier.create(repository.findByMessage("Debug")).expectNextCount(1).verifyComplete();
    }
}
