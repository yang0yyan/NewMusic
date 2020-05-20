package com.yy.music.data;

import android.media.MediaPlayer;
import android.os.Environment;

import java.util.List;
import java.util.Map;

public class GlobalVariable {
    private static GlobalVariable instance = null;

    // 私有化构造方法
    private GlobalVariable() {
        //mediaPlayer = new MediaPlayer();
    }

    public static GlobalVariable getInstance() {
        if (instance == null) {
            instance = new GlobalVariable();
        }
        return instance;
    }
    public String SEND_CONTROL = "com.example.myapplication.sendControl";
    public String SEND_DATA = "com.example.myapplication.sendData";
    public String SEND_LOCAL_DATA = "com.example.myapplication.sendLocalFragmentData";

    public String SEND_REPLAY = "com.example.myapplication.SEND_REPLAY";

    public String SCREEN_OFF = "android.intent.action.SCREEN_OFF";//
    public String SCREEN_ON = "android.intent.action.SCREEN_ON";
    public String SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";//锁屏

    public String CATCH_PATH = Environment.getExternalStorageDirectory().getPath()+"/Download";
    public String CATCH_NAME = "data.txt";
    private MediaPlayer mediaPlayer;
    public boolean SCREEN_ON_STATE = false;


    private List<Map<String,String>> list;
    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

}
