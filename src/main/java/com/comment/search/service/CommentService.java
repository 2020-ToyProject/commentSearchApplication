package com.comment.search.service;

import java.util.Optional;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.comment.search.entity.Comment;
import com.comment.search.repository.CommentRepository;

@Service
public class CommentService {
	
	private CommentRepository commentRepository;
	
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public Optional<Comment> findOne(String id){
		return commentRepository.findById(id);
	}
	
//	public List<Comment> findByProductId(String product_id){
//		return commentRepository.findByProduct_id(product_id);
//	}
	
	public Page<Comment> findDocument(String search_field, int start, int size){
		PageRequest pageRequest = PageRequest.of(start, size, Sort.Direction.DESC, "_score");
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(new MatchQueryBuilder("search_field", search_field))
				.withPageable(pageRequest)
				.build();
		
		return commentRepository.search(searchQuery);
	}
	
	public long count() {
        return commentRepository.count();
    }
	
}
