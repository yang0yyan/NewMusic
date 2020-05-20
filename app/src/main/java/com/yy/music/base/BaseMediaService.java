package com.yy.music.base;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yy.music.data.Control;

import java.io.IOException;

import static android.media.MediaPlayer.SEEK_CLOSEST_SYNC;


public abstract class BaseMediaService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener {

    public MediaPlayer mediaPlayer;

    public boolean log = true;

    public Status status = null;
    public PlayMode mode = PlayMode.ORDER;

    public enum Status {
        INIT, IDLE, END, ERROR,
        PREPARED, STARTED, PAUSED, COMPLETED, STOPPED
    }

    public enum PlayMode {
        ORDER, OYT_OF_ORDER, LOOP
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
        init();
        thread();
    }

    protected abstract void init();

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);//唤醒锁定
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        status = Status.INIT;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStartCommand(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    //准备
    public void prepareAndplay(String filePath) throws IOException {
        stop();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(filePath);
        mediaPlayer.prepareAsync();
        Control.getInstance().isPlaying(false);
    }

    //播放
    public void start() {
        if (status == Status.PREPARED || status == Status.PAUSED || status == Status.COMPLETED) {
            mediaPlayer.start();
            status = Status.STARTED;
            Control.getInstance().isPlaying(true);
        }
    }

    //暂停
    public void pause() {
        if (status == Status.STARTED) {
            mediaPlayer.pause();
            status = Status.PAUSED;
            Control.getInstance().isPlaying(false);
        }
    }

    //停止
    public void stop() {
        if (status == Status.STARTED || status == Status.PAUSED ||
                status == Status.COMPLETED || status == Status.PREPARED) {
            mediaPlayer.stop();
            status = Status.STOPPED;
            Control.getInstance().isPlaying(false);
        }
    }
    //跳转
    public void seek(long seek) {
        if (status == Status.PREPARED || status == Status.STARTED ||
                status == Status.PAUSED || status == Status.COMPLETED) {
            mediaPlayer.seekTo(seek, SEEK_CLOSEST_SYNC);
        }
    }


    public abstract void onStartCommand(Intent intent);

    @Override
    public void onPrepared(MediaPlayer mp) {

        status = Status.PREPARED;

        Control.getInstance().musicTotalTime(mp.getDuration());
        onPrepared();
    }

    public abstract void onPrepared();

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        /*if (status == Status.PREPARED || status == Status.START ||
                status == Status.PAUSED || status == Status.COMPLETED) {

        }*/
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        status = Status.COMPLETED;
        if (mode == PlayMode.LOOP) {
            mp.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        status = Status.ERROR;
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            //媒体服务器死了。在这种情况下，应用程序必须释放MediaPlayer对象并实例化一个新对象。
            release();
            initMediaPlayer();
        } else {
            //未指定的媒体播放器错误。
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //与文件或网络相关的操作错误。
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //位流不符合相关的编码标准或文件规范。
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //位流符合相关的编码标准或文件规范，但媒体框架不支持该特性。
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //有些操作耗时太长，通常超过3-5秒。
                break;
        }

        return false;
    }

    public void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            status = Status.END;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }

    public void thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (status == Status.STARTED) {
                        times();
                    }
                }

            }
        }).start();
    }

    public abstract void times();

    public void log(String TAG, String class_, String msg) {
        if (log) {
            Log.d(TAG, class_ + ": " + msg);
        }
    }
}
