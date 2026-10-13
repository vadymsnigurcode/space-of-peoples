package com.spaceofpeoples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

	@GetMapping("/api/hello")
	public String hello() {
		return "Hello, Space of Peoples!";
	}
}