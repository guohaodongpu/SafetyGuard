package com.example.ts.safetyguard.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ts.safetyguard.R;
import java.util.List;


public class WifiAdapter extends BaseAdapter {
private List<ScanResult> mWifiList;
private Context mContext;

public WifiAdapter (Context context,List<ScanResult> list) {
    this.mWifiList = list;
    this.mContext = context;
}

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mWifiList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_list,parent,false);
        ScanResult scanResult = mWifiList.get(position);
        TextView textView = view.findViewById(R.id.wifi_text_view);
        textView.setText(scanResult.SSID);
        ImageView imageView = view.findViewById(R.id.wifi_image_view);
        if (Math.abs(scanResult.level) > 80) {
            imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.wifi_low));
        } else if (Math.abs(scanResult.level) > 60 ) {
            imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.wifi_middle));
        } else {
            imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.wifi_high));
        }
        return view;
    }
}
