package com.comment.search.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName="comment", type="_doc")
public class Comment {
	
	@Id
	@Field(type=FieldType.Keyword)
	private String comment_id;
	
	@Field(type=FieldType.Keyword)
	private String url;
	
	@Field(type=FieldType.Keyword)
	private String comment_title;
	
	@Field(type=FieldType.Keyword)
	private String comment_content;
	
	@Field(type=FieldType.Keyword)
	private String product_title;
	
	@Field(type=FieldType.Integer)
	private int rating;
	
	//yyyy-MM-dd
	@Field(type=FieldType.Date)
	private Date date;
	
	@Field(type=FieldType.Keyword)
	private String product_id;
	
	@Field(type=FieldType.Keyword)
	private String product_type;
	
	@Field(type=FieldType.Text, analyzer = "bigram-analyzer")
	private String search_field;
	
	@Field(type=FieldType.Text)
	private String tms_raw_stream;
	
}
