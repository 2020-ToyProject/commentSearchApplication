package com.comment.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class PageController { 
	@RequestMapping(value = "/home") 
	public String home() { 
		return "index2"; 
	}
}

