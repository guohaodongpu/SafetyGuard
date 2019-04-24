package com.example.ts.safetyguard.controller;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * 静音模块
 * RINGER_MODE_SILENT 静音,且无振动
 * RINGER_MODE_VIBRATE 静音,但有振动
 * RINGER_MODE_NORMAL 正常声音
 * getMuteStatus() --- 判断当前是否为静音
 * setMute() --- 设置静音
 * cancelMute() --- 取消静音
 * @Author:ghd
 */
public class MuteController {
    private static final String TAG = "MuteController";
    private AudioManager mAudioManager;

    public MuteController(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean getMuteStatus() {
        int mode = mAudioManager.getRingerMode();
        boolean muteFlag = false;
        switch (mode) {
            case AudioManager.RINGER_MODE_NORMAL:
                muteFlag = false;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                muteFlag = false;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                muteFlag = true;
                break;
        }
        return muteFlag;
    }

    public boolean setMute() {
        if (mAudioManager != null) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.d(TAG, "setMute: ");
            return true;
        } else {
            return false;
        }
    }

    public boolean cancelMute() {
        if (mAudioManager != null) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            Log.d(TAG, "cancelMute: ");
            return true;
        } else {
            return false;
        }
    }

}
