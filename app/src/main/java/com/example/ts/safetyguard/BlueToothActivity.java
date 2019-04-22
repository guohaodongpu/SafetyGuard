package com.example.ts.safetyguard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ts.safetyguard.adapter.BluetoothListAdapter;

import java.util.ArrayList;

public class BlueToothActivity extends AppCompatActivity {
    private BluetoothListAdapter mAdapter;
    private ArrayList<String> mBluetoothList = new ArrayList<String>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        initData();
        initListView();
        mAdapter.notifyDataSetChanged();
    }

    private void initData() {

        mBluetoothList.clear();

        for (int i = 0; i < 10; i++) {
            mBluetoothList.add("蓝牙" + i);
        }

    }

    private void initListView() {
        mListView = findViewById(R.id.bluetooth_list_view);
        mAdapter = new BluetoothListAdapter(mBluetoothList, BlueToothActivity.this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String btName = mBluetoothList.get(position);
                Toast.makeText(BlueToothActivity.this, "当前点击的是：" + btName,
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
