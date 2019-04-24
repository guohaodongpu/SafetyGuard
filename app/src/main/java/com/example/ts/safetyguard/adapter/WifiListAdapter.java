package com.example.ts.safetyguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ts.safetyguard.R;

import java.util.ArrayList;

public class WifiListAdapter extends BaseAdapter {
    private ArrayList<String> mWifiList = new ArrayList<String>();
    private Context mContext;
    public WifiListAdapter(ArrayList<String> wifiList, Context context) {
        this.mWifiList = wifiList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mWifiList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.wifi_item,null);
        TextView textView = inflate.findViewById(R.id.wifi_name_tv_id);
        String wifiName = mWifiList.get(position);
        textView.setText(wifiName);
        return inflate;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
