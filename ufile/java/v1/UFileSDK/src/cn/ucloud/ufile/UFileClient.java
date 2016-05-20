package cn.ucloud.ufile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;

import cn.ucloud.ufile.body.ErrorBody;

import com.google.gson.Gson;
import org.apache.http.impl.client.DefaultHttpClient;

public class UFileClient {
	
	private static final String DEFAULT_CONFIG_FILE = "config.properties";
	private static final String CANONICAL_PREFIX = "X-UCloud";
	
	/*
	 * config.properties 
	 * public key
	 * private key
	 * domain suffix 
	 */
	private String ucloudPublicKey;
	private String ucloudPrivateKey;
	private String proxySuffix;
	private String downloadProxySuffix;
	private String configPath;
	private HttpClient httpClient;
	
	
	public UFileClient() {
		this.ucloudPublicKey = "";
		this.ucloudPrivateKey = "";
		this.proxySuffix = "";
		this.downloadProxySuffix = "";
		this.configPath = "";

		httpClient = new DefaultHttpClient();
		this.setHttpClient(httpClient);
	}


	public void loadConfig() {
		InputStream inputStream = null;
		try {
			if (this.configPath.equals("")) {
				inputStream = UFileClient.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
			} else {
				inputStream = new FileInputStream(this.configPath);
			}
			Properties configProperties = new Properties();     
			configProperties.load(inputStream);
			this.ucloudPublicKey = configProperties.getProperty("UCloudPublicKey");
			this.ucloudPrivateKey = configProperties.getProperty("UCloudPrivateKey");
			this.proxySuffix = configProperties.getProperty("ProxySuffix");
			this.downloadProxySuffix = configProperties.getProperty("DownloadProxySuffix");
		} catch (Exception e) {
			System.out.println("Unable to load config info: " + e.getMessage());
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
	
	public void printProperties() {
		System.out.println(this.ucloudPublicKey);
		System.out.println(this.ucloudPrivateKey);
		System.out.println(this.proxySuffix);
	}

	public String getUCloudPublicKey() {
		return ucloudPublicKey;
	}


	public void setUCloudPublicKey(String ucloudPublicKey) {
		this.ucloudPublicKey = ucloudPublicKey;
	}


	public String getUCloudPrivateKey() {
		return ucloudPrivateKey;
	}


	public void setUCloudPrivateKey(String ucloudPrivateKey) {
		this.ucloudPrivateKey = ucloudPrivateKey;
	}


	public String getProxySuffix() {
		return proxySuffix;
	}


	public void setProxySuffix(String proxySuffix) {
		this.proxySuffix = proxySuffix;
	}


	public String getDownloadProxySuffix() {
		return downloadProxySuffix;
	}


	public void setDownloadProxySuffix(String downloadProxySuffix) {
		this.downloadProxySuffix = downloadProxySuffix;
	}


	public String getConfigPath() {
		return configPath;
	}


	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}


	public void makeAuth(String stringToSign, UFileRequest request) {
		this.loadConfig();
		String signature = new HmacSHA1().sign(this.ucloudPrivateKey, stringToSign);
		String authorization = "UCloud" + " " + this.ucloudPublicKey + ":" + signature;
		request.setAuthorization(authorization);
	}


	public String spliceCanonicalHeaders(UFileRequest request) {
		Map<String, String> headers = request.getHeaders();
	    Map<String, String> sortedMap = new TreeMap<String, String>();
		
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (entry.getKey().startsWith(CANONICAL_PREFIX)) {
					sortedMap.put(entry.getKey().toLowerCase(), entry.getValue());
				}
	        }
			String result = "";
			for (Entry<String, String> entry : sortedMap.entrySet()) {
				result += entry.getKey() + ":" +  entry.getValue() + "\n";
	        }
			return result;
		} else {
			return "";
		}
	}


	public void closeErrorResponse(UFileResponse res) {
		InputStream inputStream = res.getContent();
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
			Gson gson = new Gson();
			ErrorBody body = gson.fromJson(reader, ErrorBody.class);
			String bodyJson = gson.toJson(body);
			System.out.println(bodyJson);
		
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}


	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}
}
