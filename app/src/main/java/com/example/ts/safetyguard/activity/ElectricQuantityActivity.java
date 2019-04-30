package com.example.ts.safetyguard.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ts.safetyguard.R;

public class ElectricQuantityActivity extends AppCompatActivity {
    private TextView mTextView;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_quantity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);   // 给左上角图标的左边加上一个返回的图标
        mTextView = findViewById(R.id.electric_quantity_textview);
        monitorElectricQuantity();
    }

    private void monitorElectricQuantity() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                StringBuilder sb = new StringBuilder();
                int rawlevel = intent.getIntExtra("level", -1); //电池电量
                //Log.d("level", String .valueOf(rawlevel));
                int scale = intent.getIntExtra("scale", -1);   //电池最大容量
                int status = intent.getIntExtra("status", -1);  //电池状态
                int health = intent.getIntExtra("health", -1);   //电池健康度
                int plugType = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
                int level = -1;

                if (rawlevel >= 0 && scale > 0){
                    level = (rawlevel*100)/scale;
                }
                sb.append(ElectricQuantityActivity.this.getString(R.string.battery_electric_quantity));
                sb.append(level + "%\n");

                String healthStatus = "";
                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_unknown);
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_good);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_overheat);
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_dead);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_over_voltage);
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_unspecified_failure);
                        break;
                    case BatteryManager.BATTERY_HEALTH_COLD:
                        healthStatus = ElectricQuantityActivity.this.getString(R.string.battery_health_cold);
                        break;

                }
                sb.append(ElectricQuantityActivity.this.getString(R.string.battery_health) + healthStatus + "\n");

                String batteryStatus = "";
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        batteryStatus = ElectricQuantityActivity.this.getString(R.string.battery_status_unknown);
                        break;
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        batteryStatus = ElectricQuantityActivity.this.getString(R.string.battery_status_charging);
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        batteryStatus = ElectricQuantityActivity.this.getString(R.string.battery_status_full);
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        batteryStatus = ElectricQuantityActivity.this.getString(R.string.battery_status_discharging);
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        batteryStatus = ElectricQuantityActivity.this.getString(R.string.battery_status_not_charging);
                        break;
                    default:
                        if(level <= 10)
                            sb.append(ElectricQuantityActivity.this.getString(R.string.low_battery_need_charging));
                        else if (level <= 100) {
                            sb.append(ElectricQuantityActivity.this.getString(R.string.battery_not_charging));
                        }
                        break;

                }
                sb.append(ElectricQuantityActivity.this.getString(R.string.battery_status) + batteryStatus + "\n");

                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    sb.append(ElectricQuantityActivity.this.getString(R.string.battery_charge_style));
                    switch (plugType) {
                        case BatteryManager.BATTERY_PLUGGED_AC:
                            sb.append(ElectricQuantityActivity.this.getString(R.string.battery_charge_AC));
                            break;
                        case BatteryManager.BATTERY_PLUGGED_USB:
                            sb.append(ElectricQuantityActivity.this.getString(R.string.battery_charge_USB));
                            break;
                        case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                            sb.append(ElectricQuantityActivity.this.getString(R.string.battery_charge_WIRELESS));
                    }
                }
                mTextView.setText(sb.toString());

            }
        };
        IntentFilter electricQuantityFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver,electricQuantityFilter);
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
}
