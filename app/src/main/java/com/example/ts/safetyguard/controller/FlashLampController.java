package com.example.ts.safetyguard.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * isReadyFlashlight() --- 判断设备是否有闪光灯
 */
public class FlashLampController {
    private static final String TAG = "FlashLampController";
    private Camera mCamera;
    private CameraManager manager;
    private Context mContext;

    public FlashLampController(Context context) {
        this.mContext = context;
    }

    //判断设备是否有闪光灯
    public boolean isReadyFlashlight() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public boolean isOff() {
        if (isVersionM()) {
            return manager == null;
        } else
            return mCamera == null;
    }

    //打开手电筒
    public boolean lightsOn() {

        if (isReadyFlashlight()) {
            if (isVersionM()) {
                linghtOn23(mContext);
                return true;
            } else {
                lightOn22();
                return true;
            }
        } else {
            return false;
        }

    }

    //安卓6.0以上打开手电筒
    @TargetApi(Build.VERSION_CODES.M)
    private void linghtOn23(Context context) {
        try {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            manager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //android6.0以下打开手电筒
    private void lightOn22() {
        if (mCamera == null) {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        }
    }

    public void lightsOff() {
        if (isVersionM()) {
            lightsOff23();
        } else {
            lightsOff22();
        }
    }


    //安卓6.0以下关闭手电筒
    private void lightsOff22() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.release();
            mCamera = null;
        }
    }


    //安卓6.0以上打关闭电筒
    @TargetApi(Build.VERSION_CODES.M)
    private void lightsOff23() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
            manager = null;
        } catch (Exception e) {
        }
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
