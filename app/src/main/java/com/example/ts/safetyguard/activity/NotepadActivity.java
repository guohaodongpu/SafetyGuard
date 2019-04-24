package com.example.ts.safetyguard.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ts.safetyguard.R;
import com.example.ts.safetyguard.adapter.NotepadAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView_notepad;
    private Button button_add_notepad;
    private Button button_del_notepad;
    private Button button_exit_notepad;
    private EditText edit_dialog_notepad;
    private List<String> mList;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    //数据标记
    public static int dataName;
    private String mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        findView();
        button_add_notepad.setOnClickListener(this);
        button_del_notepad.setOnClickListener(this);
        button_exit_notepad.setOnClickListener(this);

        mSharedPreferences = getSharedPreferences("notepad", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mList = new ArrayList<>();
        //异步加载已经保存的数据
        new MyAsyncTask().execute();

    }

    private void findView() {
        recyclerView_notepad = findViewById(R.id.recyclerView_notepad);
        button_add_notepad = findViewById(R.id.button_add_notepad);
        button_del_notepad = findViewById(R.id.button_del_notepad);
        button_exit_notepad = findViewById(R.id.button_exit_notepad);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_notepad: {
                showNotepadDialog();
                break;
            }
            case R.id.button_del_notepad: {
                mEditor.clear().commit();
                dataName = 0;
                mList = new ArrayList<>();
                setAdapter();
                break;
            }
            case R.id.button_exit_notepad: {
                NotepadActivity.this.finish();
                break;
            }
            default: {
                break;
            }
        }
    }

    //设置设配器
    private void setAdapter() {
        NotepadAdapter adapter = new NotepadAdapter(NotepadActivity.this,mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotepadActivity.this);
        recyclerView_notepad.setLayoutManager(linearLayoutManager);
        recyclerView_notepad.setAdapter(adapter);
    }

    //添加新的事件
    private void showNotepadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotepadActivity.this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_notepad,null);
        edit_dialog_notepad = dialogView.findViewById(R.id.edit_dialog_notepad);
        builder.setTitle("添加事件");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = edit_dialog_notepad.getText().toString();
                if (!("".equals(data) || null == data)) {
                    mEditor.putString("" + dataName,data);
                    mList.add(data);
                    mEditor.commit();
                    dataName++;
                    setAdapter();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(dialogView);
        builder.show();
    }

    private class MyAsyncTask extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MyAsyncTask","正在加载记事本");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            dataName = 0;
            while (!(mSharedPreferences.getString("" + dataName,"") == null ||
                    mSharedPreferences.getString("" + dataName,"").equals(""))) {
                mData = mSharedPreferences.getString("" + dataName,"");
                mList.add(mData);
                dataName++;
//                Log.d("MyAsyncTask","dataName=" + dataName);
            }
            setAdapter();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}