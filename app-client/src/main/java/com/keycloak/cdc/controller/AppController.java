package com.keycloak.cdc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	@GetMapping({"/","/cdc"})
	public String getApplicationPage() {
		return "index.html";		
	}
}
