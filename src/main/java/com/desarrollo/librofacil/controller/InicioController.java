package com.desarrollo.librofacil.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

	@Autowired
	// private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String inicio() {
		// System.out.println(passwordEncoder.encode("0789"));
		return "inicio.html";
	}
}
