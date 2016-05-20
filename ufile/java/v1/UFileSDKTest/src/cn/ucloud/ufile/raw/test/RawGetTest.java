package cn.ucloud.ufile.raw.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.ucloud.ufile.HmacSHA1;

public class RawGetTest {
	public static void main(String[] args)  {
		
		String path = "d:/test-download.txt";
		
		String public_key = "your ucloud public key";
		String private_Key = "your ucloud private key";
		
		String http_method = "GET";
		String content_md5 = "";
		String content_type = "text/plain";
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
		HttpGet getMethod = new HttpGet(uri);
		
		File file = new File(path);
		getMethod.setHeader("Content-Type", content_type);
		getMethod.setHeader("Authorization", authorization);
		
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = client.execute(getMethod);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		HttpEntity resEntity = response.getEntity();
		
		System.out.println(response.getStatusLine());
		if (response.getStatusLine().getStatusCode() != 200 && resEntity != null) {
			 try {
				BufferedReader br = new BufferedReader(new InputStreamReader(resEntity.getContent()));
				 String input;
				 while((input = br.readLine()) != null) {
					 System.out.println(input);
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (resEntity != null) {
			System.out.println("Response content length: " + resEntity.getContentLength());
			
			InputStream inputStream = null;
			try {
				inputStream = resEntity.getContent();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			OutputStream outputStream = null;
		    try {
		    	outputStream = new BufferedOutputStream(new FileOutputStream(file));
		        int bufSize = 1024 * 4;
		        byte[] buffer = new byte[bufSize];
		        int bytesRead;
		        while ((bytesRead = inputStream.read(buffer)) > 0) {
		        		outputStream.write(buffer, 0, bytesRead);
		        }
		    } catch (IOException e) {
		         throw new RuntimeException("Dumping data to the local");
		    } finally {
		    	 if (outputStream != null) {
		             try {
		                 outputStream.close();
		             } catch (IOException e) { throw new RuntimeException("Closing the outputStream");}
		         }
		    	 if (inputStream != null) {
		             try {
		                 inputStream.close();
		             } catch (IOException e) { throw new RuntimeException("Closing the inputStream"); }
		         }
		    }
		}
		client.getConnectionManager().shutdown();
	}
}
