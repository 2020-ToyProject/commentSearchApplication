package com.comment.search.dto;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName="comment", type="_doc")
public class CommentDto {
	
	@Field(type=FieldType.Keyword, index=false)
	private String url;
	
	@Field(type=FieldType.Keyword, index=false)
	@SerializedName("comment_title") 
	private String commentTitle;
	
	@Field(type=FieldType.Keyword, index=false)
	@SerializedName("comment_content") 
	private String commentContent;
	
	@Field(type=FieldType.Keyword, index=false)
	@SerializedName("product_title") 
	private String productTitle;
	
	@Field(type=FieldType.Integer)
	private Integer rating;
	
	@Field(type=FieldType.Date)
	private Date date;
	
	@SerializedName("comment_id") 
	private String commentId;
	
	@Field(type=FieldType.Keyword)
	@SerializedName("product_id") 
	private String productId;
	
	@Field(type=FieldType.Keyword)
	private String productType; // 아기물티슈, 강아지간식, 이어폰, 커튼
	
	@Field(type=FieldType.Text, analyzer = "bigram-analyzer")
	private String searchField; // 검색 필드로 title + content
	
	@Field(type=FieldType.Text)
	private String tmsRawStream; // 형분석 필드에서 태깅 떼고 저장
	
	@SerializedName("morph_result") 
	private String[] morphResult;
}
