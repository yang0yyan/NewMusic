package com.yy.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.yy.music.R;
import com.yy.music.Utils.PermissionUtil;
import com.yy.music.Utils.adapter.DocumentAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class DocuActivity extends AppCompatActivity {
    //根目录
    private String PATH = Environment.getExternalStorageDirectory().getPath();
    //目录
    private String PATH2 = PATH;
    private ArrayList<String> list = new ArrayList<>();
    private DocumentAdapter adapter;
    private ListView listView;
    private TextView path_tv;
    private int i = 0;
    private TextView back_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docu);
        PermissionUtil.getPermission(DocuActivity.this, PermissionUtil.READ_EXTERNAL_STORAGE);
        path_tv = findViewById(R.id.docu_path_tv);
        back_tv = findViewById(R.id.back_tv);
        listView = findViewById(R.id.doc_lv);
        adapter = new DocumentAdapter(DocuActivity.this, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        path_tv.setText(PATH2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (i == 0) {
                    String path_ = list.get(position);
                    File file = new File(PATH2, path_);
                    PATH2 = file + "";
                    path_tv.setText(file + "");
                    loadDataFrompATH(file + "");
                }
            }
        });
        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PATH2.equals(PATH)) {
                    return;
                }
                String path = PATH2.substring(0, PATH2.lastIndexOf("/")).toLowerCase();
                PATH2 = path + "";
                path_tv.setText(path + "");
                loadDataFrompATH(path + "");
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("导入本地");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFrompATH(PATH);
    }

    private void loadDataFrompATH(final String mPath) {
        list.clear();

        File file = new File(mPath);
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (!file1.isDirectory() || file1.getName().startsWith(".")) {//如果不是路径或者以 . 开头的文件夹 则直接跳过
                    continue;
                }
                list.add(file1.getName());
                //Log.i("file",file1.getName());
            }
            adapter.setData(list);//将数据载入适配器当中
        } else {
            i++;
            Log.i("file", "权限不足");
            Toast.makeText(this, "权限不足", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.doc_sure:
                ArrayList<String> list1 = new ArrayList<>();
                Map<Integer, Boolean> map = adapter.getIntemClick();
                if (map.size() != 0) {
                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(i)) {
                            File file = new File(PATH2, list.get(i));
                            list1.add(file + "");
                            Log.i("111", list1 + "");
                        }

                    }
                }
                Intent intent = new Intent();
                intent.putExtra("path", list1);
                setResult(0X02, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
