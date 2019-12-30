package com.juliuskrah.cdc.controller;

import com.juliuskrah.cdc.dto.UserDto;
import com.juliuskrah.cdc.event.EventService;
import com.juliuskrah.cdc.event.SseEvent;

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

    public ApplicationController(EventService<SseEvent> eventService) {
        this.eventService = eventService;
    }

    @Get
    Flowable<UserDto> users() {
        var user = new UserDto();
        user.setUsername("example");
        return Flowable.fromArray(new UserDto[] { user });
    }

    @Get("/stream")
    Flowable<Event<SseEvent>> events() {
        return eventService.getEvents().map(EventService::createEvent);
    }

}