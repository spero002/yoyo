package cn.ucloud.ufile.raw.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.ucloud.ufile.HmacSHA1;

public class RawDeleteTest {
	public static void main(String[] args) throws ClientProtocolException, IOException {
	
		String public_key = "your ucloud public key";
		String private_Key = "your ucloud private key";
		
		String http_method = "DELETE";
		String content_md5 = "";
		String content_type = "";
		String date = "";
		String canonicalized_ucloud_headers = "";
		String bucket = "orange";
		String key = "test-key";
		String canonicalized_resource = "/" + bucket + "/" + key;
		
		String str_to_sign =  http_method + "\n" + content_md5 + "\n" + content_type + "\n" + date + "\n" +
				 canonicalized_ucloud_headers + canonicalized_resource;
		String signature = new HmacSHA1().sign(private_Key, str_to_sign);
		System.out.println("signature: " + signature);
		
		String authorization = "UCloud" + " " + public_key + ":" + signature;
		
		System.out.println("authorization: " + authorization);
		
		String uri = "http://" + bucket + ".ufile.ucloud.cn/" + key;
		HttpDelete putMethod = new HttpDelete(uri);
	
		putMethod.setHeader("Authorization", authorization);
		
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
