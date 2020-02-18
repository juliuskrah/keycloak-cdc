package com.keycloak.cdc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	@Value("${events.streaming.url}")
	String eventsStreamingUrl;
	
	@GetMapping("/cdc")
	public String getApplicationPage(Model model) {
		model.addAttribute("EVENTS_URL", eventsStreamingUrl);
		return "index";		
	}

	@GetMapping({"/"})
	public String welcome(Model model) {
		model.addAttribute("EVENTS_URL", eventsStreamingUrl);
		return "redirect:/cdc";		
	}
}
