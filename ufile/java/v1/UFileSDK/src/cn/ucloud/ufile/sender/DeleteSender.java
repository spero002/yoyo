package cn.ucloud.ufile.sender;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class DeleteSender implements Sender {

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {
		String httpMethod = "DELETE";
		String contentMD5 = request.getContentMD5();
		String contentType = request.getContentType();	
		String date = request.getDate();
		String canonicalUCloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalResource = "/" + request.getBucketName() + "/" + request.getKey();
		String stringToSign =  httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" +
				 canonicalUCloudHeaders + canonicalResource;
		client.makeAuth(stringToSign, request);
		
	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "http://" + request.getBucketName() + ufileClient.getProxySuffix() + "/" + request.getKey();	
		HttpDelete deleteMethod = new HttpDelete(uri);
	
		HttpEntity resEntity = null;
		try {
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
		            deleteMethod.setHeader(entry.getKey(), entry.getValue());
		        }
			}
			
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(deleteMethod);
			
			resEntity = httpResponse.getEntity();
			UFileResponse ufileResponse = new UFileResponse();
			ufileResponse.setStatusLine(httpResponse.getStatusLine());
			ufileResponse.setHeaders(httpResponse.getAllHeaders());
			
			if (resEntity != null) {
				ufileResponse.setContentLength(resEntity.getContentLength());
				ufileResponse.setContent(resEntity.getContent());
			}
			return ufileResponse;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (resEntity != null && resEntity.getContent() != null) {
					resEntity.getContent().close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		return null;
	}
}
