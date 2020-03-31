package com.comment.search.service;

import java.util.List;
import java.util.Optional;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public List<Comment> findByProductId(String productId){
		return commentRepository.findByProductId(productId);
	}
	
	public Page<Comment> findDocument(String search_field, int start, int size){
		PageRequest pageRequest = PageRequest.of(start, size);
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(new MatchQueryBuilder("search_field", search_field))
				.withPageable(pageRequest)
				.withSort(new ScoreSortBuilder().order(SortOrder.DESC))
				.build();
		
		return commentRepository.search(searchQuery);
	}
	
	public long count() {
        return commentRepository.count();
    }
	
}
