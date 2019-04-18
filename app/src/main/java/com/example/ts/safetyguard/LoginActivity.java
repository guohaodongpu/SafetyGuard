package com.example.ts.safetyguard;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ts.safetyguard.util.MD5;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private ContentResolver mContentResolver;
    private final String USERURI = "content://com.example.xue.myqq.UserContentProviderprovider/user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContentResolver = LoginActivity.this.getContentResolver();
        initView();
        initEvent();
    }

    private void initView() {
        mUsernameView = findViewById(R.id.qq_username);
        mPasswordView = findViewById(R.id.qq_password);
        mSignInButton = findViewById(R.id.qq_sign_in_button);
    }

    private void initEvent() {
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    private void click() {
        String username = mUsernameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("账号不能为空");
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("密码不能为空");
        } else {
            if (signIn(username, password)) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this,
                        getString(R.string.toast_username_password_false), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean signIn(String username, String password) {
        Uri uri = Uri.parse(USERURI);
        Cursor cursor = mContentResolver.query(uri, null,
                "username = ?", new String[]{username}, null);
        String encryptedPassword = MD5.getMD5(username + password);
        while (cursor.moveToNext()){
            if(encryptedPassword.equals(cursor.getString(cursor.getColumnIndex("username")))){
                return true;
            } else {
                return false;
            }
        }
            return false;
    }

}

