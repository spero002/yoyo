package cn.ucloud.ufile.sdk.stream.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.Header;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;
import cn.ucloud.ufile.sender.DeleteSender;
import cn.ucloud.ufile.sender.GetSender;
import cn.ucloud.ufile.sender.PutSender;

public class UFilePutFileStreamTest {
	public static void main(String args[]) {
		String bucketName = "red-horse";
		String key = "put-file-stream-test";
		String filePath = "/Users/york/receipt.list";
		String saveAsPath = "/Users/york/receipt-dl.list";
		
		UFileRequest request = new UFileRequest();
		request.setBucketName(bucketName);
		request.setKey(key);
		
		try {
			request.setInputStream(new FileInputStream(filePath));
			request.setContentLength(new File(filePath).length());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		request.addHeader("Content-Type", "text/plain"); 
		
		//add some canonical headers as you need, which is not necessary
		request.addHeader("X-UCloud-World", "world");
		request.addHeader("X-UCloud-Hello", "hello");
		
		UFileClient ufileClient = null;
		
		System.out.println("PutFileStream BEGIN ...");
		try {
			ufileClient = new UFileClient();
			ufileClient.setConfigPath("/Users/york/config.properties");
			putFile(ufileClient, request);
		} finally {
			ufileClient.shutdown();
		}
		System.out.println("PutFileStream END ...\n\n");
		
		System.out.println("GetFile BEGIN...");
		try {
			ufileClient = new UFileClient();
			ufileClient.setConfigPath("/Users/york/config.properties");
			getFile(ufileClient, request, saveAsPath);
		} finally {
			ufileClient.shutdown();
		}
		System.out.println("GetFile END ...\n\n");
		
		System.out.println("DeleteFile BEGIN ...");
		try {
			ufileClient = new UFileClient();
			ufileClient.setConfigPath("/Users/york/config.properties");
			deleteFile(ufileClient, request);
		} finally {
			ufileClient.shutdown();
		}
		System.out.println("DeleteFile END ...\n\n");
	}
	
	private static void putFile(UFileClient ufileClient, UFileRequest request) {
		PutSender sender = new PutSender();
		sender.makeAuth(ufileClient, request);
		
		UFileResponse response = sender.send(ufileClient, request);

		if (response != null) {
			System.out.println("status line: " + response.getStatusLine());
			
			Header[] headers = response.getHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println("header " + headers[i].getName() + " : " + headers[i].getValue());
			}
			
			System.out.println("body length: " + response.getContentLength());
			
			InputStream inputStream = response.getContent();
			if (inputStream != null) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String s = "";
					while ((s = reader.readLine()) != null) {
						System.out.println(s);
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
				}
			}
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
					};
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
	
	private static void deleteFile(UFileClient ufileClient, UFileRequest request) {
		DeleteSender sender = new DeleteSender();
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
