package com.comment.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.comment.search.entity.Comment;

public interface CommentRepository extends ElasticsearchRepository<Comment, String> {

//	List<Comment> findByProduct_id(String product_id);
	
}
