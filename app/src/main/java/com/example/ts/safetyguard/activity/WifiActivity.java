package com.example.ts.safetyguard.activity;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ts.safetyguard.R;
import com.example.ts.safetyguard.adapter.WifiListAdapter;

import java.util.ArrayList;

public class WifiActivity extends AppCompatActivity {
    private ArrayList<String> mWifiList = new ArrayList<String>();
    private WifiListAdapter mWifiAdapter;
    private ListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
    }

    private void initData() {
        mWifiList.clear();
        for (int i = 0; i < 10; i++){
            mWifiList.add("Wifi" + i);
        }
    }

    private void initListView() {
        mListView = findViewById(R.id.wifi_list_view);
        mWifiAdapter = new WifiListAdapter(mWifiList,WifiActivity.this);
        mListView.setAdapter(mWifiAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String wifiName = mWifiList.get(position);
                Toast.makeText(WifiActivity.this,"当前点击的是:" + wifiName,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
