package com.comment.search.elasticsearch;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

import com.comment.search.dto.CommentDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * 검증되지 않음.
 * @author 남지호
 *
 */
public class HighLevelElasticConnector {
	
	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private static final Gson gson2 = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
	
	private static final String MAPPING_FILE_PATH = "./mapping.json";
	private static final String INDEX_NAME = "comment";
	
	private static final String[][] FILE_LOCATION = new String[][]{
		{"./data/allStar_강아지간식_분석.txt", "강아지간식"},
		{"./data/allStar_물티슈_분석.txt", "아기물티슈"},
		{"./data/analyzed_coopang_curtain_normalized_comments.txt", "커튼"},
		{"./data/analyzed_coopang_earphone_normalized_comments.txt", "이어폰"}
	};
	
	private RestHighLevelClient client;
	
	public HighLevelElasticConnector(RestHighLevelClient client) {
		this.client = client;
	}
	
	private String getStringFromFile(String fileName) throws IOException {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	    InputStream in = classLoader.getResourceAsStream(fileName);
	    ByteArrayOutputStream result = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = in.read(buffer)) != -1) {
	        result.write(buffer, 0, length);
	    }
	    return result.toString(StandardCharsets.UTF_8.name());
	}
	
	private boolean isIndexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName);
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}
	
	private XContentBuilder getIndexSetting() {
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
		
		return settingsBuilder;
	}
	
	public void createIndex(String indexName) throws IOException {
        if (!isIndexExist(indexName)) {
            String indexString = getStringFromFile(MAPPING_FILE_PATH);
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.source(indexString, XContentType.JSON);
            request.settings(getIndexSetting());
            client.indices().create(request, RequestOptions.DEFAULT);
        }
	}
	
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = null;
		try {
			client = new RestHighLevelClient(
			        RestClient.builder(
			                new HttpHost("localhost", 9200, "http")));
			
			HighLevelElasticConnector connector = new HighLevelElasticConnector(client);
			connector.createIndex(INDEX_NAME);
			
			for( int i = 0; i < FILE_LOCATION.length; i++ ) {
				File file = new File(FILE_LOCATION[i][0]);
				
				try(	FileReader filereader = new FileReader(file);	// 스트림 생성
				        BufferedReader bufReader = new BufferedReader(filereader);	// 버퍼 생성
				   ) {
					String line = "";
			        while((line = bufReader.readLine()) != null){
			        	CommentDto comment = null;
			        	try{
			        		comment = gson.fromJson(line, CommentDto.class);
			        	}catch(JsonSyntaxException e){
			        		comment = gson2.fromJson(line, CommentDto.class);
			        	}
			            
			            String searchField = (comment.getCommentTitle() != null && comment.getCommentTitle().length() > 0 ) ? comment.getCommentTitle() +"\n"+ comment.getCommentContent() : comment.getCommentContent();
			            comment.setSearchField(searchField);
			            String tmsRawStream = "";
			            for(int j = 0; j < comment.getMorphResult().length; j++) {
			            	tmsRawStream += comment.getMorphResult()[j].split("/")[0] + " ";
			            }
			            comment.setTmsRawStream(tmsRawStream);
			            comment.setProductType(FILE_LOCATION[i][1]);
			            
			            Type type = new com.google.gson.reflect.TypeToken<HashMap<String,Object>>(){}.getType();
			            
			            // add doc
			            Map<String, Object> commentJson = gson.fromJson(gson.toJson(comment), type);
			            
			            IndexRequest request = new IndexRequest(INDEX_NAME, "_doc", comment.getProductId() + comment.getCommentId());
			            request.source(commentJson);
			            
			            client.index(request, RequestOptions.DEFAULT);
			        }
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(client != null) client.close();
		}
		
	}

}
