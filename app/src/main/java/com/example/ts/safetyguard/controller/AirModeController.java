package com.example.ts.safetyguard.controller;

import android.content.Context;
import android.provider.Settings;

/**
 * 飞行模式
 * 因为4.0之后无法对飞行模式开启关闭进行操作，只能同步当前状态
 * getAirModeStatus() --- 获取当前飞行模式是否打开
 * @Author:ghd
 */
public class AirModeController {
    private static final String TAG = "AirModeController";
    private  Context mContext;

    public AirModeController(Context context){
        this.mContext = context;
    }

    public boolean getAirModeStatus() {
        return (Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);

    }

}
