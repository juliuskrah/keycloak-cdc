package com.juliuskrah.cdc.event;

import javax.inject.Singleton;

import io.micronaut.runtime.event.annotation.EventListener;

/**
 * Events emmitted by {@link com.juliuskrah.cdc.sink.KafkaConsumer} are handled here
 * 
 * @author Julius Krah
 */
@Singleton
public class EventHandler {
    private final EventService<SseEvent> service;

    public EventHandler(EventService<SseEvent> service) {
        this.service = service;
    }

    @EventListener
    void onEvent(final SseEvent event) {
        service.publish(event);
    }
    
}