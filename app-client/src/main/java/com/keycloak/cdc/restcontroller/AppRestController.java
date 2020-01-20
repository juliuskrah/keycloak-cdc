package com.keycloak.cdc.restcontroller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.TypeReferences.PagedModelType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.keycloak.cdc.model.User;
import com.keycloak.cdc.service.AppService;

@RestController
public class AppRestController {

	@Value("${debezium.url}")
	String DEBEZIUM_URL;
	
	@Autowired
	AppService service;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String USER_LIST_URL = "http://127.0.0.1:8082/api/users";

	final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@GetMapping("/users")
	public CollectionModel<EntityModel<User>> getUsers() {
		
		return restTemplate.exchange(USER_LIST_URL, HttpMethod.GET, null, new PagedModelType<EntityModel<User>>() {}).getBody(); 
	}
	
	@GetMapping("/notification")
	public ResponseEntity<SseEmitter> doNotify() throws InterruptedException, IOException {
		final SseEmitter emitter = new SseEmitter();
		service.addEmitter(emitter);
		service.doNotify();
		emitter.onCompletion(() -> service.removeEmitter(emitter));
		emitter.onTimeout(() -> service.removeEmitter(emitter));
		return new ResponseEntity<>(emitter, HttpStatus.OK);
	}
}
