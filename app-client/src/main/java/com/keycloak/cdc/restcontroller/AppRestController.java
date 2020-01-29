package com.keycloak.cdc.restcontroller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppRestController {

	@Value("${users.url}")
	String getUsersUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/users")
	public ResponseEntity<String> getUsers() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> users = restTemplate.exchange(getUsersUrl, HttpMethod.GET, entity, String.class);
		System.out.println(users.getBody());
		return users;
	}

}
