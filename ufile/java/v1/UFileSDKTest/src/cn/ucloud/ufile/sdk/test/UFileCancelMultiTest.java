package cn.ucloud.ufile.sdk.test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import cn.ucloud.ufile.sender.CancelMultiSender;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

/**
 * 取消分片上传
 * @author york
 *
 */
public class UFileCancelMultiTest {
	public static void main(String args[]) {
		String bucketName = "yellow-cup";
		String key = "java-put-test";
		String uploadId = "aa596121-404b-4cb3-b925-1e0614981389";
		
		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(key);
		
		//add some canonical headers as you need, which is optional
		request.addHeader("X-UCloud-World", "world");
		request.addHeader("X-UCloud-Hello", "hello");
		
		UFileClient ufileClient = null;
		try {
			ufileClient = new UFileClient();
			ufileClient.setConfigPath("/Users/york/config.properties");
			cancelUpload(ufileClient, request, uploadId);
		} finally {
			ufileClient.shutdown();
		}
	}
	
	private static void cancelUpload(UFileClient ufileClient, UFileRequest request, String uploadId) {
		CancelMultiSender sender = new CancelMultiSender(uploadId);
		sender.makeAuth(ufileClient, request);
		
		UFileResponse response = sender.send(ufileClient, request);
		if (response != null) {
			System.out.println("status line: " + response.getStatusLine());
			Header[] headers = response.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
			}
		
			System.out.println("body length: " + response.getContentLength());
			
			if (response.getContent() != null) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(response.getContent()));
					String input;
					while((input = br.readLine()) != null) {
						System.out.println(input);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}	
			}
		}
	}
}
