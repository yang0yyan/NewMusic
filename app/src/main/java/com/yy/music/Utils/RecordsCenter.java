package com.yy.music.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yy.music.data.GlobalVariable;
import com.yy.music.data.MusicBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordsCenter {
    public Activity activity;

    public RecordsCenter(Activity activity) {
        this.activity = activity;
    }

    String sd_root = Environment.getExternalStorageDirectory().getPath();

    /**
     * 动态获取权限
     * 判断是否获取权限
     */
    public void getPermissions() {
        PermissionUtil.getPermission(activity, PermissionUtil.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 初始化存储路径
     *
     * @param filepath 路径
     * @param filename 文件名  "/filename"
     * @return 存储路径
     */
    public String _initPath(String filepath, String filename) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, "com.example.myapplication.fileProvider", new File(filepath, filename));
            new Intent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            uri = Uri.fromFile(new File(filepath, filename));
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(filepath, filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            Log.e("qq", "sd = " + file);//sd = /storage/emulated/0
            return file + "";
        }
        return "error";
    }

    /**
     * 读取配置文件
     */
    public String _read(String filepath, String filename) {

        File file = new File(filepath, filename);
        if (file.toString().equalsIgnoreCase("error")) return "error";

        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            String len = "";
            while ((len = reader.readLine()) != null) {
                buffer.append(len);
            }
            is.close();
            reader.close();
            return String.valueOf(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 存储配置文件
     */
    public void _save(String filepath, String filename, String data) {
        try {
            File file = new File(filepath, filename);
            OutputStream os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查文件是否重复
     *
     * @param list1
     * @param list2
     * @return
     */
    public List<Map<String, String>> check(List<Map<String, String>> list1, List<Map<String, String>> list2) {
        List<Map<String, String>> list3 = list1;
        int i, j;
        boolean is;
        if (list1.size() == 0) {
            if (list2.size() == 0) {
                return list1;
            } else {
                return list2;
            }
        } else {
            if (list2.size() == 0) {
                return list1;
            }
        }
        for (i = 0; i < list2.size(); i++) {
            is = false;
            for (j = 0; j < list1.size(); j++) {
                String a = list1.get(j).get("filePath");
                String b = list2.get(i).get("filePath");
                assert a != null;
                if (a.equals(b)) {
                    is = true;
                }
            }
            if (!is) {
                list3.add(list2.get(i));
            }
        }
        return list1;

    }

    public List<Map<String, String>> updata() {
        List<Map<String, String>> list = new ArrayList<>();
        String asd = _read(GlobalVariable.getInstance().CATCH_PATH, GlobalVariable.getInstance().CATCH_NAME);
        if (asd == "error") return list;
        Log.i("filePath", "" + asd);
        Gson gson = new Gson();
        List<MusicBean> rs;
        Type type = new TypeToken<ArrayList<MusicBean>>() {
        }.getType();
        rs = gson.fromJson(asd, type);
        if (rs == null) {
            return list;
        }
        list.clear();
        for (int i = 0; i < rs.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("filePath", rs.get(i).getFilePath());
            map.put("fileName", rs.get(i).getFileName());
            map.put("name", rs.get(i).getName());
            map.put("album", rs.get(i).getAlbum());
            map.put("artist", rs.get(i).getArtist());
            map.put("bitrate", rs.get(i).getBitrate());
            map.put("duration", rs.get(i).getDuration());
            list.add(map);
            //Log.i("filePath",""+rs.get(i).getPath());
        }
        GlobalVariable.getInstance().setList(list);
        return list;
    }


    /**
     * 判断文件是否存在
     */
    public static boolean fileExist(String file){
        File file1 = new File(file);
        return fileExist(file1);
    }
    public static boolean fileExist(File file){
        return file.exists();
    }


}
