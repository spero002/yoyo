package cn.ucloud.ufile.raw.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.ucloud.ufile.HmacSHA1;

public class RawPostTest {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		String path = "d:/test.txt";
		//file_size
		
		String public_key = "your ucloud public key";
		String private_key = "your ucloud private key";
		
		String http_method = "POST";
		String content_md5 = "";
		String content_type = "text/plain";
		String date = "";
		String canonicalized_ucloud_headers = "";
		String bucket = "showpub";
		String key = "test-key";
		String canonicalized_resource = "/" + bucket + "/" + key;
		
		String str_to_sign =  http_method + "\n" + content_md5 + "\n" + content_type + "\n" + date + "\n" +
				 canonicalized_ucloud_headers + canonicalized_resource;
		String signature = new HmacSHA1().sign(private_key, str_to_sign);
		System.out.println("signature: " + signature);
		
		String authorization = "UCloud" + " " + public_key + ":" + signature;
		
		System.out.println("authorization: " + authorization);
		
		String uri = "http://" + bucket + ".ufile.ucloud.cn:8080/";
		HttpPost postMethod = new HttpPost(uri);
		
		File file = new File(path);
		//postMethod.addHeader("Content-Type", "multipart/form-data");
	   // postMethod.setHeader("Content-Length", String.valueOf(file.length()));
		
		ContentType ct = ContentType.create("text/plain");
		
		FileBody fileBody = new FileBody(file, ct, file.getName());
		
		HttpEntity entity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addPart("Authorization", new StringBody(authorization, ct))
				.addPart("FileName", new StringBody(key, ct))
				.addPart("file", fileBody)
				.build();
		
		postMethod.setEntity(entity);
		
		System.out.println("length = " + entity.getContentLength());
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(postMethod);
		HttpEntity resEntity = response.getEntity();
		
		System.out.println(response.getStatusLine());
		
		Header[] headers = response.getAllHeaders();
		for (Header header: headers) {
			System.out.println(header.getName() + " -> " + header.getValue());
		}
		
		if (resEntity != null) {
			System.out.println("Response content length: " + resEntity.getContentLength());
			InputStream is = resEntity.getContent(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
			}
		}
		client.getConnectionManager().shutdown();
	}
}
