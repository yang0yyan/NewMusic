package com.yy.music.Utils;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindSDUtil {
    //获取指定的单个文件夹的音乐
    public List<Map<String, String>> getFile(File file) {
        List<Map<String, String>> list = new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {

                    if (file1.isDirectory()) {
                        getFile(file1);
                    } else {
                        Map<String, String> map = music(file1,retriever);
                        if(map!=null){
                            list.add(map);
                        }
                    }
                }
            } else {
                retriever.release();
                return list;
            }
        }
        retriever.release();
        return list;
    }

    //获取指定的多个文件夹的音乐
    public List<Map<String, String>> getFile(ArrayList<String> path) {
        List<Map<String, String>> list = new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        for (int i = 0; i < path.size(); i++) {
            File file = new File(path.get(i));
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        if (file1.isDirectory()) {
                            getFile(file1);
                        } else {
                            Map<String, String> map = music(file1,retriever);
                            if(map!=null){
                                list.add(map);
                            }
                        }
                    }
                } else {
                    return list;
                }
            }
        }
        retriever.release();

        return list;
    }

    //获取指定的文件夹的图片
    public static List<Map<String, String>> getFileImg_(File path) {
        List<Map<String, String>> list = new ArrayList<>();
        if (path != null) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file1 : files) {

                    if (file1.isDirectory()) {
                        getFileImg_(file1);
                    } else {
                        if (file1.getName().endsWith(".JPG") || file1.getName().endsWith(".PNG")) {
                            Map<String, String> map = new HashMap<>();
                            map.put("fileName", file1.getName());
                            map.put("filePath", file1.getPath());
                            list.add(map);
                        }
                    }
                }
            } else {
                return list;
            }
        }

        return list;
    }



    private Map<String, String> music(File file1, MediaMetadataRetriever retriever){
        Map<String, String> map = new HashMap<>();
        if (file1.getName().endsWith(".mp3") || file1.getName().endsWith(".flac") ||
                file1.getName().endsWith(".wav")) {
            retriever.setDataSource(file1 + "");
            //名称
            String name = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            //专辑标题信息
            String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            //艺术家的信息
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            //平均比特率（以比特/秒），如果可用的话
            String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            //播放时长
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            if (name == null) {
                name = file1.getName();
            }
            if (album == null) {
                album = file1.getName();
            }

            map.put("fileName", file1.getName());
            map.put("filePath", file1.getPath());
            map.put("name", name);
            map.put("album", album);
            map.put("artist", artist);
            map.put("bitrate", bitrate);
            map.put("duration", duration);

        }else{
            map=null;
        }
        return map;
    }


}
