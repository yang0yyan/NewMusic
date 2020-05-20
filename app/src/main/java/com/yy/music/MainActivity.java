package com.yy.music;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yy.music.Utils.PermissionUtil;
import com.yy.music.Utils.RecordsCenter;
import com.yy.music.Utils.TimeUtil;
import com.yy.music.Utils.ViewPagerAdapter;
import com.yy.music.activity.LocalDocActivity;
import com.yy.music.activity.MusicActivity;
import com.yy.music.activity.MyAsd;
import com.yy.music.base.BaseActivity;
import com.yy.music.data.ActivityBean;
import com.yy.music.data.Control;
import com.yy.music.data.GlobalVariable;
import com.yy.music.databinding.ActivityMainBinding;
import com.yy.music.fragment.HomeFragment;
import com.yy.music.fragment.LocalFragment;
import com.yy.music.fragment.OnlineFragment;
import com.yy.music.model.MusicDataViewModel;
import com.yy.music.notification.MyBroadcastReceiver;
import com.yy.music.notification.NotifyBar;
import com.yy.music.service.MediaService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity extends BaseActivity implements Serializable {
    private ActivityMainBinding binding;
    private MusicDataViewModel viewModel;
    private boolean startTouch = false;
    private List<Map<String, String>> list;
    private MyBroadcastReceiver receiver;
    private boolean isPlaying;

    @Override
    protected View getLayoutId() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("title");
        actionBar.setSubtitle("subtitle");

        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        List<Fragment> list = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new OnlineFragment());
        list.add(new LocalFragment());
        listTitle.add("首页");
        listTitle.add("在线");
        listTitle.add("本地");
        adapter.setData(list, listTitle);
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        initListener();
        Intent intent = new Intent(this, MediaService.class);
        startService(intent);

    }

    private void initListener() {
        binding.musicSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            long seek = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.startTv.setText(TimeUtil.MilliToMinut(progress));
                binding.startView.setData(isPlaying, progress);
                seek = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startTouch = false;
                sendControl(0X11, seek);
            }
        });
    }

    @Override
    protected void initData() {
        PermissionUtil.getPermissions(MainActivity.this,
                PermissionUtil.READ_EXTERNAL_STORAGE,
                PermissionUtil.WRITE_EXTERNAL_STORAGE,
                PermissionUtil.ALERT_WINDOW,
                PermissionUtil.RECORD_AUDIO);

        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalVariable.getInstance().SEND_REPLAY);
        registerReceiver(receiver, filter);

        ActivityBean.owner = this;
        viewModel = new ViewModelProvider(this).get(MusicDataViewModel.class);

        RecordsCenter centre = new RecordsCenter(this);
        centre._initPath(GlobalVariable.getInstance().CATCH_PATH, GlobalVariable.getInstance().CATCH_NAME);
        list = centre.updata();
        GlobalVariable.getInstance().setList(list);

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int current = sp.getInt("music", 0);

        Control.getInstance().init(this);
        setListener();
        Intent intent = new Intent(MainActivity.this, MediaService.class);
        if (list.size() > 0) {
            if (list.size() < current) {
                current = 0;
            }
            String path = list.get(current).get("filePath");
            intent.putExtra("path", path);
            intent.putExtra("code", 1);
        }
        startService(intent);
    }

    public void setListener() {
        viewModel.getNowMusicName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.musicName.setText(s);
            }
        });
        viewModel.getMusicTotalTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                binding.musicSeek.setMax(Math.toIntExact(aLong));
                binding.musicSeek.setProgress(0);
                String time = TimeUtil.MilliToMinut(aLong);
                binding.endTv.setText(time);
                binding.startView.setMax(Math.toIntExact(aLong));
            }
        });
        viewModel.getMusicNowTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (!startTouch) {
                    binding.musicSeek.setProgress(Math.toIntExact(aLong));
                    binding.startView.setData(isPlaying, Math.toIntExact(aLong));
                }
                binding.startTv.setText(TimeUtil.MilliToMinut(aLong));
            }
        });
        viewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isPlaying = aBoolean;
                binding.startView.setData(aBoolean);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_view:
                //NotifyBar bar = new NotifyBar(this);
                /*Intent intent2 = new Intent();
                intent2.setAction(GlobalVariable.getInstance().SEND_REPLAY);
                sendBroadcast(intent2);*/
                sendControl(0X01,0);
                break;
            case R.id.next_view:
                changeMusic(1);
                break;
            case R.id.imageButton:
                Intent intent = new Intent(this, MusicActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void changeMusic(int code) {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int current = sp.getInt("music", 0);
        if (code == 1) {
            ++current;
            if (current >= list.size()) {
                current = 0;
            }
        } else if (code == 2) {
            --current;
            if (current < 0) {
                current = list.size();
            }
        }
        String path = list.get(current).get("filePath");

        editor.putInt("music", current);
        editor.apply();

        sendControl(0X02, path);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSet:
                Intent intent = new Intent(MainActivity.this, MyAsd.class);
                startActivity(intent);
                break;
            case R.id.addLocal:
                Intent intent1 = new Intent(this, LocalDocActivity.class);
                startActivity(intent1);
                break;
            case R.id.finish:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendControl(int value, long seek) {
        Intent intent = new Intent();
        intent.setAction(GlobalVariable.getInstance().SEND_CONTROL);
        intent.putExtra("control", value);
        intent.putExtra("seek", seek);
        sendBroadcast(intent);
    }

    private void sendControl(int value, String path) {
        Intent intent = new Intent();
        intent.setAction(GlobalVariable.getInstance().SEND_CONTROL);
        intent.putExtra("control", value);
        intent.putExtra("path", path);
        sendBroadcast(intent);
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                boolean is = isTaskRoot();
                Toast.makeText(this, "再按一次退出" + is, Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            }
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d("TAG", "onDestroy: ");
        /*Intent intent = new Intent(this, MediaService.class);
        stopService(intent);*/
    }
}
