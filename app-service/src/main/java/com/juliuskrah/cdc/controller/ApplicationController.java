package com.juliuskrah.cdc.controller;

import com.juliuskrah.cdc.dto.UserDto;
import com.juliuskrah.cdc.event.EventService;
import com.juliuskrah.cdc.event.SseEvent;
import com.juliuskrah.cdc.repository.ApplicationRepository;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.sse.Event;
import io.reactivex.Flowable;

/**
 * Controller class for the app service
 * 
 * @author Julius Krah
 */
@Controller("/api/users")
public class ApplicationController {
    private final EventService<SseEvent> eventService;
    private final ApplicationRepository repository;

    public ApplicationController(EventService<SseEvent> eventService, ApplicationRepository repository) {
        this.eventService = eventService;
        this.repository = repository;
    }

    @Get
    Flowable<UserDto> users() {
        return repository.findUsers();
    }

    @Get("/stream")
    Flowable<Event<SseEvent>> events() {
        return eventService.getEvents().map(EventService::createEvent);
    }

}