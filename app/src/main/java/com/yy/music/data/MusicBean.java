package com.yy.music.data;

import android.util.Log;

import java.util.List;
import java.util.Map;

public class MusicBean {



    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    private String filePath;
    private String fileName;
    private String name;
    private String album;
    private String artist;
    private String bitrate;
    private String duration;

    public String dataToJson(List<Map<String,String>> list){
        String data = "";
        StringBuffer buffer = new StringBuffer(data);
        if(list.size()==0)return "";
        buffer.append("[");
        for(int i=0;i<list.size();i++){
            buffer.append("{\"filePath\":\"" + list.get(i).get("filePath") + "\"");
            buffer.append(",\"fileName\":\"" + list.get(i).get("fileName") + "\"");
            buffer.append(",\"name\":\"" + list.get(i).get("name") + "\"");
            buffer.append(",\"album\":\"" + list.get(i).get("album") + "\"");
            buffer.append(",\"artist\":\"" + list.get(i).get("artist") + "\"");
            buffer.append(",\"bitrate\":\"" + list.get(i).get("bitrate") + "\"");
            if(i==list.size()-1){
                buffer.append(",\"duration\":\"" + list.get(i).get("duration") + "\"}");
            }else {

                buffer.append(",\"duration\":\"" + list.get(i).get("duration") + "\"},");
            }
        }
        buffer.append("]");
        Log.d("MusicBean",buffer.toString()+"");
        return  buffer.toString();
    }
}
