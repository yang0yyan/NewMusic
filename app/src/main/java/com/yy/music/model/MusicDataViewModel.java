package com.yy.music.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicDataViewModel extends ViewModel {
    private MutableLiveData<Long> musicTotalTime;//总时长
    private MutableLiveData<Long> musicNowTime;//当前时间
    private MutableLiveData<String> nowMusicName;//当前音乐名
    private MutableLiveData<String> nowMusicArtist;//当前音乐艺术家
    private MutableLiveData<Boolean> isPlaying;//当前播放状态

    public MusicDataViewModel() {
        super();
        getIsPlaying();
        getMusicNowTime();
        getMusicTotalTime();
        getNowMusicArtist();
        getNowMusicName();
    }

    public MutableLiveData<Long> getMusicTotalTime() {
        if(musicTotalTime==null){
            musicTotalTime = new MutableLiveData<>();
            musicTotalTime.setValue((long) 0);
        }
        return musicTotalTime;
    }

    public void setMusicTotalTime(Long musicTotalTime) {
        this.musicTotalTime.setValue(musicTotalTime);
    }

    public MutableLiveData<Long> getMusicNowTime() {
        if(musicNowTime==null){
            musicNowTime = new MutableLiveData<>();
            musicNowTime.setValue((long) 0);
        }
        return musicNowTime;
    }

    public void setMusicNowTime(Long musicNowTime) {
        this.musicNowTime.postValue(musicNowTime);
    }

    public MutableLiveData<String> getNowMusicName() {
        if(nowMusicName==null){
            nowMusicName = new MutableLiveData<>();
            nowMusicName.setValue("");
        }
        return nowMusicName;
    }

    public void setNowMusicName(String nowMusicName) {
        this.nowMusicName.setValue(nowMusicName);
    }

    public MutableLiveData<String> getNowMusicArtist() {
        if(nowMusicArtist==null){
            nowMusicArtist = new MutableLiveData<>();
            nowMusicArtist.setValue("");
        }
        return nowMusicArtist;
    }

    public void setNowMusicArtist(String nowMusicArtist) {
        this.nowMusicArtist.setValue(nowMusicArtist);
    }

    public MutableLiveData<Boolean> getIsPlaying() {
        if(isPlaying==null){
            isPlaying = new MutableLiveData<>();
            isPlaying.setValue(false);
        }
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying.setValue(isPlaying);
    }
}
