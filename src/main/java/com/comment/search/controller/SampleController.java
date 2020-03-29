package com.comment.search.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
	
	@RequestMapping("/")
	public String index() {
		return "hello world!!!!!!";
	}
	
	@RequestMapping("/hello")
	public String helloindex() {
		System.out.println("hello");
		return "hello world!!!";
	}
	
}