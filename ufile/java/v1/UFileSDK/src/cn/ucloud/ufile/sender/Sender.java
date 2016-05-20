package cn.ucloud.ufile.sender;

import cn.ucloud.ufile.UFileClient;
import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileResponse;

public interface Sender {

	public void makeAuth(UFileClient client, UFileRequest request);
	public UFileResponse send(UFileClient ufileClient, UFileRequest request);
}
