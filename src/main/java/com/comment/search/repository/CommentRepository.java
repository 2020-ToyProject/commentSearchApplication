package com.comment.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.comment.search.entity.Comment;

public interface CommentRepository extends ElasticsearchRepository<Comment, String> {

	List<Comment> findByProductId(String productId);
	
}
