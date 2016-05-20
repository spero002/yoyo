package com.ucloud.ulive.example.upload;

import android.os.Bundle;
import com.ucloud.live.UEasyStreaming;
import com.ucloud.live.UStreamingProfile;
import com.ucloud.ulive.example.preference.Log2FileUtil;
import com.ucloud.ulive.example.preference.Settings;


public class PublishDemo4MediaCodec extends BasePublishDemo {

    private static final String TAG = "MediaCodecPublishDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initEnv() {
        mSettings = new Settings(this);
        if (mSettings.isOpenLogRecoder()) {
            Log2FileUtil.getInstance().setLogCacheDir(mSettings.getLogCacheDir());
            Log2FileUtil.getInstance().startLog();
        }

        UStreamingProfile.Stream stream = new UStreamingProfile.Stream(rtmpPushStreamDomain, "ucloud/" + mSettings.getPusblishStreamId());

        mStreamingProfile = new UStreamingProfile.Builder()
                .setVideoCaptureWidth(mSettings.getVideoCaptureWidth())
                .setVideoCaptureHeight(mSettings.getVideoCaptureHeight())
                .setVideoEncodingBitrate(mSettings.getVideoEncodingBitRate()) //UStreamingProfile.VIDEO_BITRATE_NORMAL
                .setVideoEncodingFrameRate(mSettings.getVideoFrameRate())
                .setStream(stream).build();

        mEasyStreaming = new UEasyStreaming(this, UEasyStreaming.UEncodingType.MEDIA_CODEC);

        mEasyStreaming.setAspectWithStreamingProfile(mPreviewContainer, mStreamingProfile);

        mEasyStreaming.setStreamingStateListener(this);
    }
}
