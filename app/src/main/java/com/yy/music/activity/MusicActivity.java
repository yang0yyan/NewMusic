package com.yy.music.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.yy.music.R;
import com.yy.music.Utils.TimeUtil;
import com.yy.music.base.BaseActivity;
import com.yy.music.data.ActivityBean;
import com.yy.music.data.GlobalVariable;
import com.yy.music.databinding.ActivityMusicBinding;
import com.yy.music.model.MusicDataViewModel;

import java.util.List;
import java.util.Map;

public class MusicActivity extends BaseActivity {
    private ActivityMusicBinding binding;
    private Button start_btn;
    private TextView start_tv;
    private TextView end_tv;
    private SeekBar music_sk;
    private TextView music_name_tv;
    private MusicDataViewModel viewModel;
    private boolean startTouch;
    private List<Map<String, String>> list;
    private ViewModelStoreOwner owner;
    private MusicBroadReceiver receiver;

    @Override
    protected View getLayoutId() {
        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //start_btn = findViewById(R.id.music_start_btn);
        start_tv = findViewById(R.id.music_start_tv);
        end_tv = findViewById(R.id.music_end_tv);
        music_name_tv = findViewById(R.id.music_name_tv);
        music_sk = findViewById(R.id.music_seek_sk);
        music_sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            long seek = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.musicStartTv.setText(TimeUtil.MilliToMinut(progress));
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

        viewModel = new ViewModelProvider(ActivityBean.owner).get(MusicDataViewModel.class);
        list = GlobalVariable.getInstance().getList();

        setListener();
        receiver = new MusicBroadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalVariable.getInstance().SEND_DATA);
        registerReceiver(receiver, filter);
    }

    public void setListener() {
        viewModel.getNowMusicName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.musicNameTv.setText(s);
            }
        });
        viewModel.getMusicTotalTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                binding.musicSeekSk.setMax(Math.toIntExact(aLong));
                String time = TimeUtil.MilliToMinut(aLong);
                binding.musicEndTv.setText(time);
            }
        });
        viewModel.getMusicNowTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (!startTouch) {
                    binding.musicSeekSk.setProgress(Math.toIntExact(aLong));
                }
                binding.musicStartTv.setText(TimeUtil.MilliToMinut(aLong));
            }
        });
        viewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.musicStartBtn.setImageResource(R.mipmap.start);
                } else {
                    binding.musicStartBtn.setImageResource(R.mipmap.stop);
                }
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_start_btn:
                sendControl(0X01, 0);
                break;
            case R.id.music_last_btn:
                changeMusic(2);
                break;
            case R.id.musiic_next_btn:
                changeMusic(1);
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
                current = list.size() - 1;
            }
        }
        String path = list.get(current).get("filePath");

        editor.putInt("music", current);
        editor.apply();

        sendControl(0X02, path);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class MusicBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long endTime = intent.getLongExtra("endTime", -1);
            long startTime = intent.getLongExtra("startTime", -1);
            int status = intent.getIntExtra("status", -1);
            String musicName = intent.getStringExtra("musicName");
            if (endTime != -1) {
                music_sk.setMax((int) (endTime));
                end_tv.setText(TimeUtil.MilliToMinut(endTime * 1000));
            }
            if (startTime != -1) {
                music_sk.setProgress((int) (startTime / 1000));
                start_tv.setText(TimeUtil.MilliToMinut(startTime));
            }
            if (musicName != null) {
                music_name_tv.setText(musicName);
            }
        }
    }
}
