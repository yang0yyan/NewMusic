package com.yy.music.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.yy.music.R;
import com.yy.music.Utils.FindSDUtil;
import com.yy.music.Utils.PermissionUtil;
import com.yy.music.Utils.RecordsCenter;
import com.yy.music.Utils.adapter.LoaclDocAdapter;
import com.yy.music.data.GlobalVariable;
import com.yy.music.data.MusicBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LocalDocActivity extends AppCompatActivity {

    //根目录
    private String PATH = Environment.getExternalStorageDirectory().getPath();

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private ArrayList<String> list;
    private List<Map<String, String>> list_map;
    private ListView lv;
    private LoaclDocAdapter adapter;
    private RecordsCenter centre;
    private ProgressDialog pd2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0X01) {
                pd2.dismiss();
                Intent intent = new Intent();
                intent.setAction(GlobalVariable.getInstance().SEND_CONTROL);
                intent.putExtra("control", 0X96);
                sendBroadcast(intent);
                intent.setAction(GlobalVariable.getInstance().SEND_LOCAL_DATA);
                intent.putExtra("update", true);
                sendBroadcast(intent);
                finish();
            } else if (msg.what == 0X02) {
                adapter.setData(list_map);
                adapter.notifyDataSetChanged();
                pd2.dismiss();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loacl_doc);
        PermissionUtil.getPermission(LocalDocActivity.this, PermissionUtil.WRITE_EXTERNAL_STORAGE);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        lv = findViewById(R.id.loacl_doc_lv);
        adapter = new LoaclDocAdapter(this);
        lv.setAdapter(adapter);
        list_map = new ArrayList<Map<String, String>>();
        list = new ArrayList<>();
        list.add(PATH);

        pd2 = new ProgressDialog(this);
        pd2.setTitle("data loading");
        pd2.setMessage("data is loading...");
        pd2.setCancelable(false);
        pd2.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        centre = new RecordsCenter(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                if (btn1.getText().equals("开始扫描")) {
                    if (list.size() != 0) {
                        pd2.show();
                        Thread_(list);
                        btn1.setText("确认添加");
                    }
                } else {
                    //btn1.setText("开始扫描");

                    pd2.show();

                    updata();

                }
                break;
            case R.id.button2:
                Intent intent = new Intent(LocalDocActivity.this, DocuActivity.class);
                startActivityForResult(intent, 0X01);
                break;
            case R.id.button3:
                /*String asd = centre._read(GlobalVariable.getInstance().CATCH_PATH,GlobalVariable.getInstance().CATCH_NAME);

                Gson gson = new Gson();
                List<MusicBean> rs=new ArrayList<MusicBean>();
                Type type = new TypeToken<ArrayList<MusicBean>>() {}.getType();
                rs=gson.fromJson(asd, type);*/
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0X01) {
            if (resultCode == 0X02) {
                list.clear();
                list = data.getStringArrayListExtra("path");
                /*Log.i("onActivityResult", list + "");
                setData(list);*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.doc_sure:
                Intent intent = new Intent();
                intent.setAction(GlobalVariable.getInstance().SEND_DATA);
                intent.putExtra("status", 99);
                sendBroadcast(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updata() {

        List<Map<String, String>> list1 = GlobalVariable.getInstance().getList();
        Thread_(list1, list_map);

    }


    private void Thread_(final List<Map<String, String>> list1, final List<Map<String, String>> list2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list_map = centre.check(list1, list2);
                MusicBean bean = new MusicBean();
                String data = bean.dataToJson(list_map);
                centre._save(GlobalVariable.getInstance().CATCH_PATH, GlobalVariable.getInstance().CATCH_NAME, data);
                GlobalVariable.getInstance().setList(list_map);
                Message msg = new Message();
                msg.what = 0X01;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void Thread_(final ArrayList<String> list1) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                list_map.clear();
                FindSDUtil util = new FindSDUtil();
                list_map = util.getFile(list1);
                Message msg = new Message();
                msg.what = 0X02;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if (pd2 != null) {
            pd2.dismiss();
        }
        super.onDestroy();

    }
}
