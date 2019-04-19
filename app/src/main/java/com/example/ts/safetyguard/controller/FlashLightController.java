package com.example.ts.safetyguard.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * isReadyFlashLight() --- 判断设备是否有闪光灯
 */
public class FlashLightController {
    private static final String TAG = "FlashLightController";
    private Camera mCamera;
    private CameraManager manager;
    private Context mContext;

    public FlashLightController(Context context) {
        this.mContext = context;
    }

    //判断设备是否有闪光灯
    public boolean isReadyFlashLight() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public boolean getFlashLightStatus() {
        if (isVersionM()) {
            return manager == null;
        } else
            return mCamera == null;
    }

    //打开手电筒
    public boolean lightsOn() {
        if (isReadyFlashLight()) {
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

    public boolean lightsOff() {
        if (isReadyFlashLight()) {
            if (isVersionM()) {
                lightsOff23();
                return true;
            } else {
                lightsOff22();
                return true;
            }
        } else {
            return false;
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
