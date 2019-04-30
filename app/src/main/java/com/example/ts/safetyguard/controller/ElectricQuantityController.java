package com.example.ts.safetyguard.controller;

import android.content.Context;
import android.content.Intent;

import com.example.ts.safetyguard.R;

public class ElectricQuantityController {
    private static final String TAG = "ElectricQuantityController";
    private Context mContext;
    private StringBuilder mElectricQuantity;

    public ElectricQuantityController(Context context) {
        this.mContext = context;
    }

    public String getElectricQuantity(Intent intent) {
        mElectricQuantity = new StringBuilder();
        int rawlevel = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        int level = -1;

        if(rawlevel >= 0 && scale > 0){
            level = (rawlevel*100)/scale;
        }
        mElectricQuantity.append(mContext.getString(R.string.title_electric_quantity));
        mElectricQuantity.append(level + "%\n");

        return mElectricQuantity.toString();
    }
}
