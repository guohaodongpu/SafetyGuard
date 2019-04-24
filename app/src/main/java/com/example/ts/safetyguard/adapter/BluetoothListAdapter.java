package com.example.ts.safetyguard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ts.safetyguard.R;
/**
 * @Author:ghd
 */
import java.util.ArrayList;

public class BluetoothListAdapter extends BaseAdapter {

    private ArrayList<String> mBluetoothList = new ArrayList<String>();
    private Context mContext;

    public BluetoothListAdapter(ArrayList<String> btList, Context context) {
        this.mBluetoothList = btList;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View inflate = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.bluetooth_item, null);
        TextView textView = inflate.findViewById(R.id.bluetooth_name_tv_id);// 查找item中的textView
        String btName = mBluetoothList.get(position);
        textView.setText(btName);
        return inflate;
    }

    @Override
    public int getCount() {
        // ListView的行数
        return mBluetoothList.size();
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