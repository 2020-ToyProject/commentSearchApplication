package com.comment.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class CommentSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentSearchApplication.class, args);
	}
	
}
