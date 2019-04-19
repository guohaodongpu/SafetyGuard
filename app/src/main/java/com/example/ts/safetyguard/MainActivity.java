package com.example.ts.safetyguard;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
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

import com.example.ts.safetyguard.controller.AirModeController;
import com.example.ts.safetyguard.controller.BluetoothController;
import com.example.ts.safetyguard.controller.MuteController;
import com.zjun.progressbar.CircleDotProgressBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private NavigationView mNavigationView;
    private CircleDotProgressBar mCircleDotProgressBar;
    private ImageButton mBluetoothImageButton;
    private ImageButton mMuteImageButton;
    private BluetoothController mBluetoothController;
    private MuteController mMuteController;
    private AirModeController mAirModeController;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, bluetoothFilter);

        getDoNotDisturb();

        initController();
        initView();
        initEvent();
        updateAllIcon();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_wifi: {
                break;
            }
            case R.id.nav_bluetooth: {
                break;
            }
            case R.id.nav_electric_quantity: {
                break;
            }
            case R.id.nav_mute: {
                break;
            }
            case R.id.nav_brightness: {
                break;
            }
            case R.id.nav_sound_volume: {
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
        mNavigationView = findViewById(R.id.nav_view);
        mCircleDotProgressBar = findViewById(R.id.score_seek_bar);
        mBluetoothImageButton = findViewById(R.id.on_off_bluetooth_bt);
        mMuteImageButton = findViewById(R.id.on_off_mute_bt);
    }

    private void initEvent() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mBluetoothImageButton.setOnClickListener(this);
        mMuteImageButton.setOnClickListener(this);
    }

    private void initController() {
        mBluetoothController = new BluetoothController();
        mMuteController = new MuteController(MainActivity.this);
    }

    private void updateAllIcon() {
        updateBluetoothIcon();
        updateMuteIcon();
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
        public void onReceive(Context context, Intent intent) {
            //蓝牙
            int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (bluetoothState) {
                case BluetoothAdapter.STATE_OFF: {
                    updateBluetoothIcon();
                    break;
                }
                case BluetoothAdapter.STATE_ON: {
                    updateBluetoothIcon();
                    break;
                }
                default: {
                    break;
                }
            }
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
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //蓝牙
            case R.id.on_off_bluetooth_bt: {
                showBluetoothToast();
                break;
            }

            //静音
            case R.id.on_off_mute_bt: {
                showMuteToast();
                break;
            }
            default: {
                break;
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
    protected void onRestart() {
        super.onRestart();
        updateAllIcon();
    }
}
