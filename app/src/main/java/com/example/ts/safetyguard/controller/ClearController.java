package com.example.ts.safetyguard.controller;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * killProcess() --- 杀死后台进程
 * 最终结果：
 * 在使用killBackgroundProcesses()方法时，系统会在需要的时候再次重启被杀死的进程。而真正可以杀死进程的方法是forceStopPackage(),但是该方法被标记为@hide，而且注释翻译得知，第三方的应用是无法调用该方法的。最后翻阅资料得知，如果想在第三方的APP上使用和调用该方法，不仅需要获取一个系统的签名，并且还要把自己的应用设置成"android:sharedUserId="android.uid.system" 权限。
 * getAvailMemory() --- 获取当前可用内存
 * getTotalMemory() --- 获取总内存
 * @Author:ghd
 */
public class ClearController {

    private Context mContext;

    public ClearController(Context context) {
        this.mContext = context;
    }

    public void killProcess() {
        ActivityManager activityManger = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
        if (list != null)
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                String[] pkgList = apinfo.pkgList;
                if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    for (int j = 0; j < pkgList.length; j++) {
                        boolean flag = pkgList[j].contains("com.example.ts.safetygurad");
                        //判断是否为当前应用，要不然也可能会结束当前应用。
                        if (!flag) {
                            activityManger.killBackgroundProcesses(pkgList[j]);
                        }
                    }
                }
            }

    }

    public long getAvailMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024);
    }

    public long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        return initial_memory / (1024 * 1024);
    }

    public int getScore(){
        Log.d("getAvailMemory", String.valueOf(getAvailMemory()));
        Log.d("getTotalMemory", String.valueOf(getTotalMemory()));
        float scoreF=(float)getAvailMemory()/(float)getTotalMemory()*100;
        Log.d("scoreF", String.valueOf(scoreF));
        return (int)scoreF;
    }

}
