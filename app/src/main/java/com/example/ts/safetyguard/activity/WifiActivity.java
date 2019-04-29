package com.example.ts.safetyguard.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.ts.safetyguard.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WifiActivity extends AppCompatActivity {
    private ArrayList<String> mWifiList = new ArrayList<String>();
    private ListView mListView;
    private Button mButton;
    private WifiManager mWifiManager;
    private List<ScanResult> mScanResults;
    private ArrayAdapter mAdapter;
    private Timer mTimer = new Timer(true);
    boolean mFlag;
    int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initEvent();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiList.clear();
                Log.d("WifiListClear", String.valueOf(mWifiList.size()));
                registerPermission();
                mFlag = false;
            }
        });

        if (!mWifiManager.isWifiEnabled()) {
            Toast.makeText(this ,"Wifi",Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true);
        }

       mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mFlag != true){
                    Intent intent = new Intent();
                    intent.setAction("performClick");
                    sendBroadcast(intent);
                }
            }
        }, 0, 5000);

    }

    private void initEvent() {
        mFlag = true;
        mListView = findViewById(R.id.wifi_list_view);
        mButton = findViewById(R.id.scan_button);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mAdapter = new ArrayAdapter<>(WifiActivity.this, android.R.layout.simple_list_item_1,mWifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("performClick");
        registerReceiver(receiver,intentFilter);
    }

    private void scanWifi() {
        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(receiver,intentFilter);*/
        Toast.makeText(this,"Scanning...",Toast.LENGTH_SHORT).show();
        //Android 9.0 将 WiFiManager 的 startScan() 方法标为了废弃，
        // 前台应用 2 分钟内只能使用 4 次startScan()，后台应用 30 分钟内只能调用 1次 startScan()，
        // 否则会直接返回 false 并且不会触发扫描操作。
        mWifiManager.startScan();
        Log.d("startscan", String.valueOf(mWifiManager.startScan()) );
        mScanResults = mWifiManager.getScanResults();
        Log.d("mScanResults", String.valueOf(mScanResults.size()));

        for (ScanResult sr : mScanResults){
            mWifiList.add(sr.SSID + "   " + "信号强度" + sr.level);
        }
        Log.d("mWifiList", String.valueOf(mWifiList.size()));

        mAdapter.notifyDataSetChanged();//界面重绘，保留原有位置、数据信息
        mListView.setAdapter(mAdapter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mButton.performClick();
        }
    };



    private void registerPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);

        } else {
            if (mWifiManager.isWifiEnabled()) {
                scanWifi();
            } else {
                mFlag = true;
                mTimer.cancel();
                mWifiManager.setWifiEnabled(true);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            scanWifi();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        unregisterReceiver(receiver);
    }
}
