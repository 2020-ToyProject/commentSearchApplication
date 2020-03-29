package com.comment.search.elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.comment.search.dto.CommentDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ElasticSearchConnector {
	private static TransportClient client = null;
	private static final String host = "192.168.99.100";
	private static final int tc_port = 9300;
	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static final String indexName = "comment";
	private static final String[][] fileLocation = new String[][]{
		{"C:\\ToyProject\\crawling_data\\allStar_강아지간식_분석.txt", "강아지간식"},
		{"C:\\ToyProject\\crawling_data\\allStar_물티슈_분석.txt", "아기물티슈"},
		{"C:\\ToyProject\\crawling_data\\analyzed_coopang_curtain_normalized_comments.txt", "커튼"},
		{"C:\\ToyProject\\crawling_data\\analyzed_coopang_earphone_normalized_comments.txt", "이어폰"}
	};
	
	public void start(){
		try {
			Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new TransportAddress(InetAddress.getByName(host), tc_port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void end() throws IOException{
		client.close();
	}
	
	public void addMapping(String fileName) throws IOException, URISyntaxException{
		URL mappingUrl = this.getClass().getResource(fileName);
		String mappingStr = FileUtils.readFileToString(new File(mappingUrl.toURI()));
		
		PutMappingRequestBuilder pmrb = client.admin().indices().preparePutMapping(indexName);

		pmrb.setType("_doc");
		pmrb.setSource(mappingStr, XContentType.JSON);
		pmrb.execute().actionGet();
	}
	
	public void createIndex(String indexName){
		XContentBuilder settingsBuilder = null;
		try {
			settingsBuilder = XContentFactory.jsonBuilder()
			        .startObject()
			            .field("number_of_shards",3)
			            .field("number_of_replicas",1)
			            
			            .startObject("analysis")
			                .startObject("tokenizer")
			                    .startObject("bigram-tokenizer")
			                        .field("type","nGram")
			                        .field("min_gram","2")
			                        .field("max_gram","2")
			                        .array("token_chars",new String[]{"letter","digit"})
			                    .endObject()
			                .endObject()
			                
			                .startObject("analyzer")
			                    .startObject("bigram-analyzer")
			                        .field("type","custom")
			                        .field("tokenizer","bigram-tokenizer")
			                    .endObject()
			                .endObject()
			            .endObject()
			        .endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 간단한 setting 일 경우
//		Settings indexSettings = Settings.builder()
//                .put("number_of_shards", 3)
//                .put("number_of_replicas", 1)
//                .build();
		
		CreateIndexRequest indexRequest = new CreateIndexRequest(indexName, Settings.EMPTY);
		indexRequest.settings(settingsBuilder);
		client.admin().indices().create(indexRequest).actionGet();
	}
	
	public void addDocument(CommentDto comment) throws IOException{
		IndexResponse response = client.prepareIndex(indexName, "_doc", comment.getProductId() + comment.getCommentId())
		        .setSource(jsonBuilder()
		                    .startObject()
		                    	.field("url", comment.getUrl())
		                        .field("comment_title", comment.getCommentTitle())
		                        .field("comment_content", comment.getCommentContent())
		                        .field("product_title", comment.getProductTitle())
		                        .field("rating", comment.getRating())
		                        .field("date", comment.getDate())
		                        .field("comment_id", comment.getCommentId())
		                        .field("product_id", comment.getProductId())
		                        .field("product_type", comment.getProductType())
		                        .field("search_field", comment.getSearchField())
		                        .field("tms_raw_stream", comment.getTmsRawStream())
		                    .endObject()
		                  )
		        .get();
		
		// json string으로 색인하는 방법으로 doc id 명시가 필요 없음 그러나 search_fiel
//		String json = "{" +
//		        "\"user\":\"kimchy\"," +
//		        "\"postDate\":\"2013-01-30\"," +
//		        "\"message\":\"trying out Elasticsearch\"" +
//		    "}";
//		IndexResponse response2 = client.prepareIndex("test", "_doc")
//		        .setSource(json, XContentType.JSON)
//		        .get();
	}
	
	public static void main(String[] args) throws IOException{
		ElasticSearchConnector con = new ElasticSearchConnector();
		// start
		con.start();
		// create index
		con.createIndex(indexName);
		// add mapping
		try {
			con.addMapping("/mapping.json");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		// read file
		for( int i = 0; i < fileLocation.length; i++ ) {
			File file = new File(fileLocation[i][0]);
	        FileReader filereader = new FileReader(file);	// 스트림 생성
	        BufferedReader bufReader = new BufferedReader(filereader);	// 버퍼 생성
	        String line = "";
	        while((line = bufReader.readLine()) != null){
//	            System.out.println(line);
	        	CommentDto comment = null;
	        	try{
	        		comment = gson.fromJson(line, CommentDto.class);
	        	}catch(JsonSyntaxException e){
	        		Gson gson2 = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
	        		comment = gson2.fromJson(line, CommentDto.class);
	        	}
	            
	            String searchField = (comment.getCommentTitle() != null && comment.getCommentTitle().length() > 0 ) ? comment.getCommentTitle() +"\n"+ comment.getCommentContent() : comment.getCommentContent();
	            comment.setSearchField(searchField);
	            String tmsRawStream = "";
	            for(int j = 0; j < comment.getMorphResult().length; j++) {
	            	tmsRawStream += comment.getMorphResult()[j].split("/")[0] + " ";
	            }
	            comment.setTmsRawStream(tmsRawStream);
	            comment.setProductType(fileLocation[i][1]);
	            // add doc
	    		con.addDocument(comment);
	        }
	        bufReader.close();
		}
		
		// end
		con.end();
		System.out.println("끝");
	}
}
