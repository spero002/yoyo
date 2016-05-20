package cn.ucloud.ufile.raw.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.ucloud.ufile.HmacSHA1;

public class RawPutTest {
	public static void main(String[] args) throws ClientProtocolException, IOException {

		String path = "d:/test.txt";
		
		String public_key = "your ucloud public key";
		String private_Key = "your ucloud private key";
		
		String http_method = "PUT";
		String content_md5 = "";
		String content_type = "text/plain";
		String date = "";
		String canonicalized_ucloud_headers = "x-ucloud-hello" + ":" + "hello" + "\n" +
				"x-ucloud-world" + ":" + "world" + "\n";
		//String canonicalized_ucloud_headers = "";
		String bucket = "helloworld";
		String key = "test-key";
		String canonicalized_resource = "/" + bucket + "/" + key;
		
		String str_to_sign =  http_method + "\n" + content_md5 + "\n" + content_type + "\n" + date + "\n" +
				 canonicalized_ucloud_headers + canonicalized_resource;
		String signature = new HmacSHA1().sign(private_Key, str_to_sign);
		System.out.println("signature: " + signature);
		
		String authorization = "UCloud" + " " + public_key + ":" + signature;
		
		System.out.println("authorization: " + authorization);
		
		String uri = "http://" + bucket + ".ufile.ucloud.cn/" + key;
		HttpPut putMethod = new HttpPut(uri);
		
		File file = new File(path);
		putMethod.setHeader("Content-Type", content_type);
		putMethod.setHeader("Authorization", authorization);
		putMethod.setHeader("X-UCloud-Hello", "hello");
		putMethod.setHeader("X-UCloud-World", "world");
		
		InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file), file.length());
		
		putMethod.setEntity(entity);
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(putMethod);
		HttpEntity resEntity = response.getEntity();
		
		System.out.println(response.getStatusLine());
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
