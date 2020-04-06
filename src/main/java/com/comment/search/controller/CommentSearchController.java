package com.comment.search.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comment.search.entity.Comment;
import com.comment.search.service.CommentService;

@RestController
public class CommentSearchController {

	private final static int DEFAULT_DOCUMENT_SIZE = 10;
	private final static int DEFAULT_DOCUMENT_START = 0;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping(value="/search")
	public ResponseEntity<Page<Comment>> findDocument(
			@RequestParam(value = "start") Integer start,
			@RequestParam(value = "size") Integer size,
			@RequestParam(value = "keyword") String keyword
			){
		if(keyword == null || keyword.length() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Page<Comment> comments = commentService.findDocument(keyword, 
				(start == null || start <= 0) ? DEFAULT_DOCUMENT_START : start, 
				(size == null || size <= 0) ? DEFAULT_DOCUMENT_SIZE : size);
		
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}
	
	@GetMapping(value="/search/id")
	public ResponseEntity<Comment> findDocumentById(
			@RequestParam(value = "id") String id
			){
		if(id == null || id.length() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		Optional<Comment> comment = commentService.findOne(id);
		
		if(!comment.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(comment.get());
	}
	
}
