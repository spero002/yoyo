package com.ucloud.ulive.example.upload;

import android.os.Bundle;
import com.ucloud.live.UEasyStreaming;
import com.ucloud.live.UStreamingProfile;
import com.ucloud.ulive.example.preference.Log2FileUtil;
import com.ucloud.ulive.example.preference.Settings;


public class PublishDemo4X264 extends BasePublishDemo {

//    private static final String TAG = "PublishDemo4X264";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initEnv() {
        mSettings = new Settings(this);
        if (mSettings.isOpenLogRecoder()) {
            Log2FileUtil.getInstance().setLogCacheDir(mSettings.getLogCacheDir());
            Log2FileUtil.getInstance().startLog(); //
        }

        UStreamingProfile.Stream stream = new UStreamingProfile.Stream(rtmpPushStreamDomain, "ucloud/" + mSettings.getPusblishStreamId());

        mStreamingProfile = new UStreamingProfile.Builder()
                .setVideoCaptureWidth(mSettings.getVideoCaptureWidth())
                .setVideoCaptureHeight(mSettings.getVideoCaptureHeight())
                .setVideoEncodingBitrate(mSettings.getVideoEncodingBitRate()) //UStreamingProfile.VIDEO_BITRATE_NORMAL
                .setVideoEncodingFrameRate(mSettings.getVideoFrameRate())
                .setStream(stream).build();

        mEasyStreaming = new UEasyStreaming(this, UEasyStreaming.UEncodingType.MEDIA_X264);
        mEasyStreaming.setStreamingStateListener(this);
        mEasyStreaming.setAspectWithStreamingProfile(mPreviewContainer, mStreamingProfile);
    }
}
