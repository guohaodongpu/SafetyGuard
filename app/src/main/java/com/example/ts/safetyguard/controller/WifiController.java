package com.example.ts.safetyguard.controller;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Wifi模块
 * isReadyWifi() --- 是否支持Wifi
 * getWifiStatus() --- 获取当前手机的Wifi状态
 * openWifi() --- 开启Wifi
 * closeWifi() --- 关闭Wifi
 */
public class WifiController {
    private static final String TAG = "WifiController";
    private WifiManager mWifiManager;

    public WifiController(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    //是否支持Wifi
    public boolean isReadyWifi() {
        if (mWifiManager != null) {
            return true;
        } else {
            return false;
        }
    }

    //获取当前手机的Wifi状态
    public boolean getWifiStatus() {
        if (isReadyWifi()) {
            return mWifiManager.isWifiEnabled();
        } else {
            return false;
        }
    }

    //打开Wifi
    public boolean openWifi() {
        if (isReadyWifi() && !getWifiStatus()) {
            mWifiManager.setWifiEnabled(true);
            //Log.d(TAG,"openWifi: ");
            return true;
        } else {
            return false;
        }
    }

    //关闭Wifi
    public boolean closeWifi() {
        if (isReadyWifi() &&getWifiStatus()) {
            mWifiManager.setWifiEnabled(false);
            //Log.d(TAG,"closeWifi: ");
            return true;
        } else {
            return false;
        }
    }
}