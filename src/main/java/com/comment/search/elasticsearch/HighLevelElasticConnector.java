package com.comment.search.elasticsearch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * 검증되지 않음.
 * @author 남지호
 *
 */
public class HighLevelElasticConnector {
	
	private static final String MAPPING_FILE_PATH = "./src/main/resources/mapping.json";
	
	public static String getStringFromFile(String fileName) throws IOException {
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
	
	public static boolean isIndexExist(RestHighLevelClient client, String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName);
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}
	
	public static void createIndex(RestHighLevelClient client, String indexName) throws IOException {
        if (!isIndexExist(client, indexName)) {
            String indexString = getStringFromFile(MAPPING_FILE_PATH);
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            request.source(indexString, XContentType.JSON);
            client.indices().create(request, RequestOptions.DEFAULT);
        }
}

	public static void main(String[] args) {
	}

}
