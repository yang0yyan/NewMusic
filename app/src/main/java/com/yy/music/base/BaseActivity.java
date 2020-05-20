package com.yy.music.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yy.music.R;
import com.yy.music.Utils.StatusBarUtil;

import java.util.Arrays;

public abstract class BaseActivity extends AppCompatActivity {
    public boolean log = true;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        StatusBarUtil.setStatusBarColor(this, R.color.colorLucency);
        initView();
        initData();
    }

    protected abstract View getLayoutId();

    protected abstract void initView();

    protected abstract void initData();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0XFF) {
            Log.d("TAG", "onRequestPermissionsResult: " + Arrays.toString(permissions));
            Log.d("TAG", "onRequestPermissionsResult: " + Arrays.toString(grantResults));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void log(String TAG, String class_, String msg) {
        if (log) {
            Log.d(TAG, class_ + ": " + msg);
        }
    }
}
