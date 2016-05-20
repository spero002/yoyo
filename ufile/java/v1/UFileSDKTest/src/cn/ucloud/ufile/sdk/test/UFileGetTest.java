package cn.ucloud.ufile.sdk.test;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import cn.ucloud.ufile.sender.GetSender;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

/**
 * 下载文件测试
 * @author york
 *
 */
public class UFileGetTest {
	public static void main(String args[]) {
		String bucketName = "red-horse";
		String key = "post-test";
		String saveAsPath = "/Users/york/receipt-get.list";  
		
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
			getFile(ufileClient, request, saveAsPath);
		} finally {
			ufileClient.shutdown();
		}
	}
	
	private static void getFile(UFileClient ufileClient, UFileRequest request, String saveAsPath) {
		GetSender sender = new GetSender();
		sender.makeAuth(ufileClient, request);
		
		UFileResponse response = sender.send(ufileClient, request);
		if (response != null) {
			
			System.out.println("status line: " + response.getStatusLine());
		
			Header[] headers = response.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
			}
		
			System.out.println("body length: " + response.getContentLength());
			
			//handler error response 
			if (response.getStatusLine().getStatusCode() != 200 && response.getContent() != null) {
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
					if (response.getContent() != null) {
						try {
							response.getContent().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					inputStream = response.getContent();
					outputStream = new BufferedOutputStream(new FileOutputStream(saveAsPath));
			        int bufSize = 1024 * 4;
			        byte[] buffer = new byte[bufSize];
			        int bytesRead;
			        while ((bytesRead = inputStream.read(buffer)) > 0) {
			        	outputStream.write(buffer, 0, bytesRead);
			        }
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
