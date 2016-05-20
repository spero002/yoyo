package cn.ucloud.ufile.sender;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public class FinishMultiSender implements Sender {

	private String uploadId;
	private SortedMap<Integer, String> eTags;
	private String newKey;

	public FinishMultiSender(String uploadId, SortedMap<Integer, String> eTags,
			String newKey) {
		this.uploadId = uploadId;
		this.eTags = eTags;
		this.newKey = newKey;
	}

	@Override
	public void makeAuth(UFileClient client, UFileRequest request) {
		String httpMethod = "POST";
		String contentMD5 = request.getContentMD5();
		/* Post StringEntity default Content-Type */
		String contentType = "text/plain; charset=ISO-8859-1";
		String date = request.getDate();
		String canonicalUCloudHeaders = client.spliceCanonicalHeaders(request);
		String canonicalResource = "/" + request.getBucketName() + "/"
				+ request.getKey();
		String stringToSign = httpMethod + "\n" + contentMD5 + "\n"
				+ contentType + "\n" + date + "\n" + canonicalUCloudHeaders
				+ canonicalResource;

		// attach the contentType

		client.makeAuth(stringToSign, request);

	}

	@Override
	public UFileResponse send(UFileClient ufileClient, UFileRequest request) {
		String uri = "http://" + request.getBucketName()
				+ ufileClient.getProxySuffix() + "/" + request.getKey()
				+ "?uploadId=" + this.uploadId + "&newKey=" + this.newKey;
		HttpPost postMethod = new HttpPost(uri);

		HttpEntity resEntity = null;
		try {
			Map<String, String> headers = request.getHeaders();
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					postMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}

			String serialEtags = serialEtagsWithComma(eTags);

			StringEntity reqEntity = new StringEntity(serialEtags);

			postMethod.setEntity(reqEntity);
			UFileResponse ufileResponse = new UFileResponse();
			HttpResponse httpResponse = ufileClient.getHttpClient().execute(
					postMethod);

			resEntity = httpResponse.getEntity();
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

	private String serialEtagsWithComma(SortedMap<Integer, String> eTags) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> entry : eTags.entrySet()) {
			System.out.println("key: " + entry.getKey() + " value: "
					+ entry.getValue());
			sb.append(entry.getValue());
			sb.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
