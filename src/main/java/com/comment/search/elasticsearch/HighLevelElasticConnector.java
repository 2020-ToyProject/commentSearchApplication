package com.comment.search.elasticsearch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * 검증되지 않음.
 * @author 남지호
 *
 */
public class HighLevelElasticConnector {
	
	private static final String MAPPING_FILE_PATH = "./src/main/resources/mapping.json";
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
	
	public static void main(String[] args) {
//		HighLevelElasticConnector connector = new HighLevelElasticConnector();
		
	}

}
