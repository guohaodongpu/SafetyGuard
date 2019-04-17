package com.example.ts.safetyguard.controller;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

/**
 * 蓝牙模块
 * isReadyBluetooth() --- 是否支持蓝牙
 * getBluetoothStatus() --- 获取当前手机的蓝牙状态
 * openBluetooth() --- 开启蓝牙
 * closeBluetooth() --- 关闭蓝牙
 */
public class BluetoothController {
    private static final String TAG = "BluetoothController";
    private BluetoothAdapter mBluetoothAdapter;

    public BluetoothController() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //是否支持蓝牙
    public boolean isReadyBluetooth() {
        if (mBluetoothAdapter != null) {
            return true;
        } else {
            return false;
        }
    }

    //获取当前手机的蓝牙状态
    public boolean getBluetoothStatus() {
        if (isReadyBluetooth()) {
            return mBluetoothAdapter.isEnabled();
        } else {
            return false;
        }
    }

    //打开蓝牙
    public boolean openBluetooth(){
        if (isReadyBluetooth() && !getBluetoothStatus()){
            mBluetoothAdapter.enable();
            Log.d(TAG, "openBluetooth: ");
            return true;
        } else {
            return false;
        }

    }

    //关闭蓝牙
    public boolean closeBluetooth(){
        if (isReadyBluetooth() && getBluetoothStatus()){
            mBluetoothAdapter.disable();
            Log.d(TAG, "closeBluetooth: ");
            return true;
        } else {
            return false;
        }
    }


}
