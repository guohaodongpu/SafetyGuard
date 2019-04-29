package com.example.ts.safetyguard.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ts.safetyguard.R;

public class SettingActivity extends AppCompatActivity {
    private TextView mTextView_alarm_volume;
    private TextView mTextView_music_volume;
    private TextView mTextView_voice_call_volume;
    private TextView mTextView_ring_volume;
    private SeekBar mSeekBar_alarm;
    private SeekBar mSeekBar_music;
    private SeekBar mSeekBar_voice_call;
    private SeekBar mSeekBar_ring;
    private AudioManager mAudioManager;
    private static final int TYPE_1 = AudioManager.STREAM_ALARM;
    private static final int TYPE_2 = AudioManager.STREAM_MUSIC;
    private static final int TYPE_3 = AudioManager.STREAM_VOICE_CALL;
    private static final int TYPE_4 = AudioManager.STREAM_RING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findView();

        initVolume();

        changeVolume(mSeekBar_alarm,TYPE_1,mTextView_alarm_volume);
        changeVolume(mSeekBar_music,TYPE_2,mTextView_music_volume);
        changeVolume(mSeekBar_voice_call,TYPE_3,mTextView_voice_call_volume);
        changeVolume(mSeekBar_ring,TYPE_4,mTextView_ring_volume);

    }
    //应用切换切换时重新加载
    @Override
    protected void onRestart() {
        super.onRestart();
        initVolume();
    }


    /**
     * 音量调节
     * mAudioManager.setStreamVolume(type,index,flags);
     *          type
     * AudioManager.STREAM_ALARM = 闹钟音量 = TYPE_1 最低音量为1
     * AudioManager.STREAM_MUSIC = 媒体音量 = TYPE_2
     * AudioManager.STREAM_VOICE_CALL = 通话音量 = TYPE_3 最低音量为1
     * AudioManager.STREAM_RING =铃声音量 = TYPE_4
     *          index
     * 音量值
     *          flags
     * AudioManager.FLAG_SHOW_UI = 改变时 显示音量条 右侧栏出现 几秒钟后消失
     * AudioManager.FLAG_PLAY_SOUND = 改变时 播放声音
     */

    private int nowVolume(int type) {
        //查看当前音量
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int systemVolume = mAudioManager.getStreamVolume(type);
//            Log.d("tips","音量为:"+systemVolume);
        return systemVolume;
    }

    //加载系统音量
    private void initVolume() {
        mTextView_alarm_volume.setText("" + nowVolume(TYPE_1));
        mSeekBar_alarm.setProgress(mAudioManager.getStreamVolume(TYPE_1));
        mTextView_music_volume.setText("" + nowVolume(TYPE_2));
        mSeekBar_music.setProgress(mAudioManager.getStreamVolume(TYPE_2));
        mTextView_voice_call_volume.setText("" + nowVolume(TYPE_3));
        mSeekBar_voice_call.setProgress(mAudioManager.getStreamVolume(TYPE_3));
        mTextView_ring_volume.setText("" + nowVolume(TYPE_4));
        mSeekBar_ring.setProgress(mAudioManager.getStreamVolume(TYPE_4));
    }

    private void changeVolume(SeekBar seekBar, final int type, final TextView textView) {
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(mAudioManager.getStreamMaxVolume(type));
        seekBar.setMin(mAudioManager.getStreamMinVolume(type));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAudioManager.setStreamVolume(type,seekBar.getProgress(),AudioManager.FLAG_PLAY_SOUND);
                textView.setText("" + nowVolume(type));
            }
        });
    }

    //获取控件ID
    private void findView() {
        mTextView_alarm_volume = findViewById(R.id.textView_alarm_volume);
        mTextView_music_volume = findViewById(R.id.textView_music_volume);
        mTextView_voice_call_volume = findViewById(R.id.textView_voice_call_volume);
        mTextView_ring_volume = findViewById(R.id.textView_ring_volume);
        mSeekBar_alarm = findViewById(R.id.seekBar_alarm);
        mSeekBar_music = findViewById(R.id.seekBar_music);
        mSeekBar_voice_call = findViewById(R.id.seekBar_voice_call);
        mSeekBar_ring = findViewById(R.id.seekBar_ring);
    }

    //音量键 控制媒体音量 max=15 min=0
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (mAudioManager.getStreamVolume(TYPE_2) > 0) {
                mAudioManager.adjustStreamVolume(TYPE_2,AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND);
                mSeekBar_music.setProgress(mAudioManager.getStreamVolume(TYPE_2));
                mTextView_music_volume.setText("" + nowVolume(TYPE_2));
                return true;
            }

        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (mAudioManager.getStreamVolume(TYPE_2) < 15) {
                mAudioManager.adjustStreamVolume(TYPE_2,AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
                mSeekBar_music.setProgress(mAudioManager.getStreamVolume(TYPE_2));
                mTextView_music_volume.setText("" + nowVolume(TYPE_2));
                return true;
            }
        }else {

        }
        return super.onKeyDown(keyCode, event);
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
