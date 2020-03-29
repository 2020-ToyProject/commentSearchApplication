package com.comment.search.dto;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class CommentDto {
	private String url;
	@SerializedName("comment_title") private String commentTitle;
	@SerializedName("comment_content") private String commentContent;
	@SerializedName("product_title") private String productTitle;
	private Integer rating;
	private Date date;
	@SerializedName("comment_id") private String commentId;
	@SerializedName("product_id") private String productId;
	private String productType; // 아기물티슈, 강아지간식, 이어폰, 커튼
	private String searchField; // 검색 필드로 title + content
	private String tmsRawStream; // 형분석 필드에서 태깅 떼고 저장
	@SerializedName("morph_result") private String[] morphResult;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getTmsRawStream() {
		return tmsRawStream;
	}
	public void setTmsRawStream(String tmsRawStream) {
		this.tmsRawStream = tmsRawStream;
	}
	public String[] getMorphResult() {
		return morphResult;
	}
	public void setMorphResult(String[] morphResult) {
		this.morphResult = morphResult;
	}
	
}
