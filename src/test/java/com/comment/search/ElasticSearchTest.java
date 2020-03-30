package com.comment.search;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.comment.search.entity.Comment;
import com.comment.search.repository.CommentRepository;
import com.comment.search.service.CommentService;

@SpringBootTest
public class ElasticSearchTest {

	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	public void setup() {
		commentService = new CommentService(commentRepository);
	}

//	@Test
//	public void indexingAndSearch() {
//		Comment comment = new Comment("436430871", "http://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=15031758&trTypeCd=20&trCtgrNo=585021", 
//				"abcd", 
//				"으....안에 머리카락 보이나요?ㅠㅠ 상품 보내주실 때 좀 더 꼼꼼히 해주세요", 
//				"BEST11번가대표 애견간식 강아지간식 강아지껌 개껌 시저 강아지캔 츄르 네츄럴코어", 
//				1, 
//				"2019-01-01", 
//				"15031758", 
//				"강아지 간식", 
//				"으....안에 머리카락 보이나요?ㅠㅠ 상품 보내주실 때 좀 더 꼼꼼히 해주세요");
//		
//		commentService.save(comment);
//		
//		Comment searched = commentService.findOne("436430871").get();
//		
//		Assertions.assertEquals(searched.getCommentId(), comment.getCommentId());
//	}
	
	@Test
	public void searchById() {
		Optional<Comment> comment = commentService.findOne("436430874");
		
		Assertions.assertEquals(comment.isPresent(), true);
		
		if(comment.isPresent()) {
			System.out.println(comment.get().getComment_id());
		}
		
	}
	
	@Test
	public void searchBySearchField() {
		Page<Comment> comments = commentService.findDocument("곰팡이", 0, 10);
		Assertions.assertNotNull(comments);
		System.out.println(comments.getTotalElements());
		System.out.println(comments.getTotalPages());
	}
	
}
