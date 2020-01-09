package com.juliuskrah.cdc.event;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.inject.Singleton;

import io.micronaut.http.sse.Event;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Utility class to create events
 * 
 * @author Julius Krah
 */
@Singleton
public class EventService<T> {
    private final Subject<T> subject = PublishSubject.create();
    
    public static Event<SseEvent> createEvent(final SseEvent event) {
        return Event.of(event)
            .id(UUID.randomUUID().toString())
            .retry(Duration.of(5, ChronoUnit.SECONDS));
    }

    public void publish(final T event) {
        subject.onNext(event);
    }

    public Flowable<T> getEvents() {
        return subject.hide().toFlowable(BackpressureStrategy.BUFFER);
    }
}