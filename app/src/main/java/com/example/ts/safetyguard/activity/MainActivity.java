package com.example.ts.safetyguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ts.safetyguard.LoginActivity;
import com.example.ts.safetyguard.R;
import com.example.ts.safetyguard.controller.AirModeController;
import com.example.ts.safetyguard.controller.BluetoothController;
import com.example.ts.safetyguard.controller.ElectricQuantityController;
import com.example.ts.safetyguard.controller.FlashLightController;
import com.example.ts.safetyguard.controller.MuteController;
import com.example.ts.safetyguard.controller.WifiController;
import com.zjun.progressbar.CircleDotProgressBar;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        View.OnLongClickListener {

    private NavigationView mNavigationView;
    private CircleDotProgressBar mCircleDotProgressBar;
    private ImageButton mBluetoothImageButton;
    private ImageButton mMuteImageButton;
    private ImageButton mFLashLightImageButton;
    private ImageButton mWifiImageButton;
    private WifiController mWifiController;
    private MuteController mMuteController;
    private BluetoothController mBluetoothController;
    private AirModeController mAirModeController;
    private FlashLightController mFlashLightController;
    private ElectricQuantityController mElectricQuantityController;
    private Toast mToast;
    private Menu mActivityMainDrawerMenu;
    private MenuItem mAirModeMenuItem;
    private MenuItem mBluetoothMenuItem;
    private MenuItem mWifiMenuItem;
    private MenuItem mElectricQuantityMenuItem;
    private Timer mScoreSeekBarTimer;
    private TimerTask mTimerTask;
    private boolean isProgressGoing;
    private int mScoreSeekBarProgress;
    private int mScoreSeekBarMax;
    private String mElectricQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initIntentFilter();
        getDoNotDisturb();
        initController();
        initScoreSeekBarData();
        initView();
        initEvent();
        updateAllIcon();
        updateAllTitle();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //右上角菜单点击事件
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_wifi_id: {
                break;
            }
            case R.id.nav_bluetooth_id: {
                if (mBluetoothController.isReadyBluetooth()) {
                    Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.toast_not_supported_bluetooth));
                }
                break;
            }
            case R.id.nav_electric_quantity_id: {
                Intent intent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                startActivity(intent);
                break;
            }
            case R.id.nav_air_mode_id: {
                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);
                break;
            }
            case R.id.nav_brightness: {
                break;
            }
            case R.id.nav_sound_volume: {
                break;
            }
            case R.id.nav_memorandum_book: {
                break;
            }
            case R.id.nav_sign_out: {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("您已退出，请重新登录");
                builder.setCancelable(false); //设置弹窗不可取消
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent reIntent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(reIntent);
                    }
                });
                builder.show();
                break;
            }
            default: {
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {
        mNavigationView = findViewById(R.id.nav_view_id);
        mActivityMainDrawerMenu = mNavigationView.getMenu();
        mCircleDotProgressBar = findViewById(R.id.score_seek_bar_id);
        mBluetoothImageButton = findViewById(R.id.on_off_bluetooth_bt_id);
        mMuteImageButton = findViewById(R.id.on_off_mute_bt_id);
        mFLashLightImageButton = findViewById(R.id.on_off_flashlight_bt_id);
        mWifiImageButton = findViewById(R.id.on_off_wifi_bt_id);
        mAirModeMenuItem = mActivityMainDrawerMenu.findItem(R.id.nav_air_mode_id);
        mWifiMenuItem = mActivityMainDrawerMenu.findItem(R.id.nav_wifi_id);
        mBluetoothMenuItem = mActivityMainDrawerMenu.findItem(R.id.nav_bluetooth_id);
        mElectricQuantityMenuItem = mActivityMainDrawerMenu.findItem(R.id.nav_electric_quantity_id);
    }

    private void initEvent() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mBluetoothImageButton.setOnClickListener(this);
        mMuteImageButton.setOnClickListener(this);
        mFLashLightImageButton.setOnClickListener(this);
        mWifiImageButton.setOnClickListener(this);
        mCircleDotProgressBar.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isProgressGoing) {
                    if (mScoreSeekBarProgress == mScoreSeekBarMax) {
                        mScoreSeekBarProgress = 0;
                        mCircleDotProgressBar.setProgress(mScoreSeekBarProgress);
                    }
                    startProgress();
                } else {
                    stopProgress();
                }
            }
        });
    }

    private void initIntentFilter() {
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, bluetoothFilter);
        IntentFilter wifiFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, wifiFilter);
        IntentFilter airModeFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(receiver, airModeFilter);
        IntentFilter electricQuantityFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,electricQuantityFilter);
    }

    private void initScoreSeekBarData() {
        mScoreSeekBarMax = 100;
        readyProgress();
    }

    private void readyProgress() {
        if (mScoreSeekBarTimer == null) {
            mScoreSeekBarTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!isProgressGoing) {
                        return;
                    }
                    if (++mScoreSeekBarProgress >= mScoreSeekBarMax) {
                        mScoreSeekBarProgress = mScoreSeekBarMax;
                        mCircleDotProgressBar.setProgress(mScoreSeekBarProgress);
                        stopProgress();
                        return;
                    }
                    mCircleDotProgressBar.setProgress(mScoreSeekBarProgress);
                }
            };
        }
    }

    private void startProgress() {
        isProgressGoing = true;
        stopTimerTask();
        readyProgress();
        mScoreSeekBarTimer.schedule(mTimerTask, 1000, 100);
    }

    private void stopTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mScoreSeekBarTimer != null) {
            mScoreSeekBarTimer.cancel();
        }
        mTimerTask = null;
        mScoreSeekBarTimer = null;
    }

    private void stopProgress() {
        isProgressGoing = false;
        stopTimerTask();
    }

    private void initController() {
        mBluetoothController = new BluetoothController();
        mMuteController = new MuteController(MainActivity.this);
        mAirModeController = new AirModeController(MainActivity.this);
        mFlashLightController = new FlashLightController(MainActivity.this);
        mWifiController = new WifiController(MainActivity.this);
        mElectricQuantityController = new ElectricQuantityController(MainActivity.this);
    }

    private void updateAllIcon() {
        updateBluetoothIcon();
        updateMuteIcon();
        updateWifiIcon();
        updateFlashLightIcon();
    }

    private void updateAllTitle() {
        updateAirModeTitle();
        updateBluetoothTitle();
    }

    private void updateAirModeTitle() {
        if (mAirModeController.getAirModeStatus()) {
            mAirModeMenuItem.setTitle(getString(R.string.title_air_mode_on));
        } else {
            mAirModeMenuItem.setTitle(getString(R.string.title_air_mode_off));
        }
    }

    private void updateBluetoothTitle() {
        if (mBluetoothController.getBluetoothStatus()) {
            mBluetoothMenuItem.setTitle(getString(R.string.title_bluetooth_on));
        } else {
            mBluetoothMenuItem.setTitle(getString(R.string.title_bluetooth_off));
        }

    }
    private void updataElectricQuantityTitle() {
        mElectricQuantityMenuItem.setTitle(mElectricQuantity);
    }

    private void updateMuteIcon() {
        if (mMuteController.getMuteStatus()) {
            mMuteImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_menu_mute));
        } else {
            mMuteImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_icon_no_mute));
        }
    }

    private void updateFlashLightIcon() {
        if (mFlashLightController.getFlashLightStatus()) {
            mFLashLightImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_icon_flashlight_open));
        } else {
            mFLashLightImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_icon_flashlight_close));
        }
    }

    private void updateWifiIcon() {
        if (mWifiController.getWifiStatus()) {
            mWifiImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_wifi_isopen));
        } else {
            mWifiImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_wifi));
        }
    }

    private void updateBluetoothIcon() {
        if (mBluetoothController.getBluetoothStatus()) {
            mBluetoothImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_icon_bluetooth_isopen));
        } else {
            mBluetoothImageButton.setImageDrawable(getResources().
                    getDrawable(R.drawable.ic_menu_bluetooth));
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            //Wifi
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    mWifiImageButton.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_menu_wifi));
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    mWifiImageButton.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_icon_wifi_isopen));
                    break;
                default:
                    break;
            }

            //蓝牙
            int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (bluetoothState) {
                case BluetoothAdapter.STATE_OFF: {
                    updateBluetoothIcon();
                    updateBluetoothTitle();
                    break;
                }
                case BluetoothAdapter.STATE_ON: {
                    updateBluetoothIcon();
                    updateBluetoothTitle();
                    break;
                }
                default: {
                    break;
                }
            }
            //静音
            if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                final int ringerMode = am.getRingerMode();
                switch (ringerMode) {
                    case AudioManager.RINGER_MODE_NORMAL: {
                        updateMuteIcon();
                        break;
                    }
                    case AudioManager.RINGER_MODE_VIBRATE: {
                        updateMuteIcon();
                        break;
                    }
                    case AudioManager.RINGER_MODE_SILENT: {
                        updateMuteIcon();
                        break;
                    }
                }
            }
            //飞行模式 0关闭；1开启
            if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {//飞行模式状态改变
                updateAirModeTitle();
            }
            //电量
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) { //电池电量改变
                mElectricQuantity = mElectricQuantityController.getElectricQuantity(intent);
                updataElectricQuantityTitle();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //Wifi
            case R.id.on_off_wifi_bt_id: {
                showWifiToast();
                break;
            }

            //蓝牙
            case R.id.on_off_bluetooth_bt_id: {
                showBluetoothToast();
                break;
            }

            //静音
            case R.id.on_off_mute_bt_id: {
                showMuteToast();
                break;
            }

            //手电筒
            case R.id.on_off_flashlight_bt_id: {
                showFlashLightToast();
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            //Wifi
            case R.id.on_off_wifi_bt_id: {
                showWifiToast();
                break;
            }

            //蓝牙
            case R.id.on_off_bluetooth_bt_id: {
                if (mBluetoothController.isReadyBluetooth()) {
                    Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(intent);
                } else {
                    showToast(getString(R.string.toast_not_supported_bluetooth));
                }
                break;
            }

            //静音
            case R.id.on_off_mute_bt_id: {
                Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                startActivity(intent);
                break;
            }

        }
        return true;
    }

    private void showFlashLightToast() {
        if (mFlashLightController.getFlashLightStatus()) {
            if (mFlashLightController.lightsOff()) {
                updateFlashLightIcon();
                showToast(getString(R.string.toast_close_flash_light_success));
            } else {
                showToast(getString(R.string.toast_close_flash_light_fail));
            }
        } else {
            if (mFlashLightController.lightsOn()) {
                updateFlashLightIcon();
                showToast(getString(R.string.toast_open_flash_light_success));
            } else {
                showToast(getString(R.string.toast_open_flash_light_fail));
            }

        }


    }

    private void showWifiToast() {
        if (!mWifiController.getWifiStatus()) {
            if (mWifiController.openWifi()) {
                showToast(getString(R.string.toast_open_wifi_success));
            } else {
                showToast(getString(R.string.toast_open_wifi_fail));
            }
        } else {
            if (mWifiController.closeWifi()) {
                showToast(getString(R.string.toast_close_wifi_success));
            } else {
                showToast(getString(R.string.toast_close_wifi_fail));
            }
        }
    }

    private void showBluetoothToast() {
        if (!mBluetoothController.getBluetoothStatus()) {
            if (mBluetoothController.openBluetooth()) {
                updateBluetoothIcon();
                showToast(getString(R.string.toast_open_bluetooth_success));
            } else {
                showToast(getString(R.string.toast_open_bluetooth_fail));
            }
        } else {
            if (mBluetoothController.closeBluetooth()) {
                showToast(getString(R.string.toast_close_bluetooth_success));
                updateBluetoothIcon();
            } else {
                showToast(getString(R.string.toast_close_bluetooth_fail));
            }
        }
    }

    private void showMuteToast() {
        if (mMuteController.getMuteStatus()) {
            if (mMuteController.cancelMute()) {
                updateMuteIcon();
                showToast(getString(R.string.toast_cancel_mute_success));
            } else {
                showToast(getString(R.string.toast_cancel_mute_fail));
            }
        } else {
            if (mMuteController.setMute()) {
                updateAllIcon();
                showToast(getString(R.string.toast_set_mute_success));
            } else {
                showToast(getString(R.string.toast_set_mute_fail));
            }

        }
    }

    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private void getDoNotDisturb() {
        NotificationManager notificationManager =
                (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(android.provider.Settings
                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateAllIcon();
        updateAllTitle();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAllIcon();
        updateAllTitle();
    }

}
