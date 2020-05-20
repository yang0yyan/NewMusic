package com.yy.music.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.yy.music.Utils.RecordsCenter;
import com.yy.music.base.BaseMediaService;
import com.yy.music.data.Control;
import com.yy.music.data.GlobalVariable;

import java.io.File;
import java.io.IOException;

public class MediaService extends BaseMediaService {
    String TAG = "MediaService";

    private MediaServiceBroad receiver;
    private int code;

    @Override
    protected void init() {
        //注册广播
        receiver = new MediaServiceBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalVariable.getInstance().SEND_CONTROL);
        registerReceiver(receiver, filter);
        log = true; //是否打印log
    }

    @Override
    public void onStartCommand(Intent intent) {
        String path = intent.getStringExtra("path");
        code = intent.getIntExtra("code",0);
        log(TAG, "onStartCommand", path);
        //Log.d(TAG, "onStartCommand: " + path);
        prepare(path);
    }

    @Override
    public void onPrepared() {
        if(code==0){
            start();
        }
    }

    private void prepare(String path){
        if (path != null) {
            File file = new File(path);
            Control.getInstance().nowMusicName(file.getName());
            try {
                if (RecordsCenter.fileExist(path))
                    prepareAndplay(path);
                else
                    Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void times() {
        long position = mediaPlayer.getCurrentPosition();
        Control.getInstance().musicNowTime(position);
    }





    private void control(int control, Intent intent) {
        switch (control) {
            case 0X02:
                String path = intent.getStringExtra("path");
                code = intent.getIntExtra("code",0);
                prepare(path);
                break;
            case 0X01:
                if (status == Status.STARTED)
                    pause();
                else
                    start();
                break;
            case 0X00:

                break;
            case 0X11:
                long seek = intent.getLongExtra("seek", 0);
                seek(seek);
                break;
        }
    }

    class MediaServiceBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("control", 0) != 0) {
                control(intent.getIntExtra("control", 0), intent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
