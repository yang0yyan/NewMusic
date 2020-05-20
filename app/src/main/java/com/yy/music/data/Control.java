package com.yy.music.data;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.yy.music.model.MusicDataViewModel;

public class Control implements BaseView {
    private static Control instance = null;
    private ViewModelStoreOwner owner;
    private MusicDataViewModel viewModel;

    private Control() {
        super();
        this.owner = owner;
    }
    public static Control getInstance(){
        if (instance==null){
            synchronized (Control.class){
                if (instance==null){
                    instance = new Control();
                }
            }
        }
        return instance;
    }

    public void init(Activity owner) {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) owner).get(MusicDataViewModel.class);
    }


    @Override
    public void musicTotalTime(long musicTotalTime) {
        viewModel.setMusicTotalTime(musicTotalTime);
    }

    @Override
    public void musicNowTime(long musicNowTime) {
        viewModel.setMusicNowTime(musicNowTime);
    }

    @Override
    public void nowMusicName(String nowMusicName) {
        viewModel.setNowMusicName(nowMusicName);
    }

    @Override
    public void nowMusicArtist(String nowMusicArtist) {

    }

    @Override
    public void isPlaying(boolean isPlaying) {
        viewModel.setIsPlaying(isPlaying);
    }
}
