package com.yy.music.data;

public interface BaseView {
    void musicTotalTime(long musicTotalTime);
    void musicNowTime(long musicNowTime);
    void nowMusicName(String nowMusicName);
    void nowMusicArtist(String nowMusicArtist);
    void isPlaying(boolean isPlaying);
}
