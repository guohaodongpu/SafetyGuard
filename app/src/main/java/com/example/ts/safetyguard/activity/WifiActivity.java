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

public class WifiActivity extends AppCompatActivity {
    private ArrayList<String> mWifiList = new ArrayList<String>();
    private ListView mListView;
    private Button mButton;
    private WifiManager mWifiManager;
    private List<ScanResult> mScanResults;
    private ArrayAdapter mAdapter;
    int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        initListView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPermission();
            }
        });
        if(!mWifiManager.isWifiEnabled()) {
            Toast.makeText(this ,"Wifi",Toast.LENGTH_LONG).show();
            mWifiManager.setWifiEnabled(true);
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mWifiList);
        mListView.setAdapter(mAdapter);
    }


    private void initListView() {
        mListView = findViewById(R.id.wifi_list_view);
        mButton = findViewById(R.id.scan_button);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    private void scanWifi() {
        mWifiList.clear();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(receiver,intentFilter);
        mWifiManager.startScan();
        Toast.makeText(this,"Scanning...",Toast.LENGTH_SHORT).show();

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mScanResults = mWifiManager.getScanResults();
            Log.d("mScanResults", String.valueOf(mScanResults.size()));

            for(ScanResult sr : mScanResults){
                mWifiList.add(sr.SSID+ "   " + "信号强度" + sr.level  );
                mAdapter.notifyDataSetChanged();//界面重绘，保留原有位置、数据信息
            }
            mScanResults.clear();
        }
    };

    private void registerPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);

        } else {
            scanWifi();
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
        unregisterReceiver(receiver);
    }
}
