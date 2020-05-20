package com.yy.music.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yy.music.R;
import com.yy.music.Utils.PermissionUtil;
import com.yy.music.data.GlobalVariable;
import com.yy.music.view.MyVisualizerView;

import java.util.ArrayList;
import java.util.List;

public class MyAsd extends AppCompatActivity {
    private String PATH = Environment.getExternalStorageDirectory().getPath();
    private LinearLayout ll;
    private MediaPlayer mediaPlayer;
    private List<Short> reverbName = new ArrayList<Short>();
    private List<String> reverbVals = new ArrayList<String>();
    private Equalizer equalizer;
    private int j = 0;
    private Visualizer visualizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.getPermission(MyAsd.this, PermissionUtil.RECORD_AUDIO);
        actionBarSetting();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        setContentView(ll);
        mediaPlayer = GlobalVariable.getInstance().getMediaPlayer();
        if (mediaPlayer != null) {
            int request = ContextCompat.checkSelfPermission(MyAsd.this,
                    PermissionUtil.RECORD_AUDIO);
            if (request == PackageManager.PERMISSION_GRANTED) {//缺少权限，进行权限申请
                setupVisualizer();
            } else {
                //权限同意 已授权
            }
            setupEqualizer();

            setupBassBoost();

            setupPresetReverb();

        }


    }

    private void setupVisualizer() {
        final MyVisualizerView visualizerView = new MyVisualizerView(this);

        visualizerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (120f * getResources().getDisplayMetrics().density)));
        ll.addView(visualizerView);
        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        if (visualizer == null) return;
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            //波形数据
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                if (j < 10) {
                    Log.i("onWaveFormDataCapture", "被调用了" + j);
                    j++;
                }
                visualizerView.updataVisualizer(waveform);
            }

            //频率数据
            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                if (j < 10) {
                    Log.i("onFftDataCapture", "被调用了");
                    j++;
                }
                visualizerView.updataVisualizer(fft);
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    private void setupEqualizer() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        equalizer.setEnabled(true);
        int aa = equalizer.getNumberOfBands();//获取预设的数量
        short[] bb = equalizer.getBandLevelRange();//返回最大最小波段级别
        short cc = equalizer.getBandLevel((short) 1);//获得音乐的增益等级
        int dd = equalizer.getCenterFreq((short) 1);//获得波段的中心频率
        int[] ee = equalizer.getBandFreqRange((short) 2);//获取波段的频率范围。
        int ff = equalizer.getBand(1);//受频率影响最大的频带
        int gg = equalizer.getCurrentPreset();//获得当前预设
        int ii = equalizer.getNumberOfPresets();//获取支持的预置数量
        String hh = equalizer.getPresetName((short) 1);//获取基于索引的预置名称

        /*if(1==1){
            return;
        }*/

        TextView eqTitle = new TextView(this);
        eqTitle.setText("均衡器：");
        ll.addView(eqTitle);
        //获取均衡器的最小值

        //获取均衡器支持的所有频率
        final short bands = equalizer.getNumberOfBands();
        for (short i = 0; i < bands; i++) {
            final int minEQLevel = equalizer.getBandLevelRange()[0];
            //获取均衡器的最大值
            int maxEQLevel = equalizer.getBandLevelRange()[1];
            //
            final TextView eqTextView = new TextView(this);
            eqTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            eqTextView.setText((equalizer.getCenterFreq(i) / 1000) + " HZ");
            ll.addView(eqTextView);
            //
            LinearLayout eqLl = new LinearLayout(this);
            eqLl.setOrientation(LinearLayout.HORIZONTAL);
            //
            TextView minDbTextView = new TextView(this);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEQLevel / 100) + " dB");
            //
            TextView maxDbTextView = new TextView(this);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQLevel / 100) + " dB");
            //
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;
            SeekBar seekBar = new SeekBar(this);
            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(maxEQLevel - minEQLevel);
            seekBar.setProgress(equalizer.getBandLevel(i) - minEQLevel);

            final short brand = i;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    equalizer.setBandLevel(brand, (short) (progress + minEQLevel));
                    eqTextView.setText((progress + minEQLevel) / 100 + " HZ");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            eqLl.addView(minDbTextView);
            eqLl.addView(seekBar);
            eqLl.addView(maxDbTextView);
            ll.addView(eqLl);

        }
    }

    private void setupBassBoost() {
        final BassBoost bassBoost = new BassBoost(0, mediaPlayer.getAudioSessionId());
        bassBoost.setEnabled(true);

        TextView bbTextView = new TextView(this);
        bbTextView.setText("重低音： ");
        ll.addView(bbTextView);

        SeekBar bbSeekBar = new SeekBar(this);
        bbSeekBar.setMax(1000);
        bbSeekBar.setProgress(0);
        bbSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassBoost.setStrength((short) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ll.addView(bbSeekBar);

    }

    private void setupPresetReverb() {
        final PresetReverb presetReverb = new PresetReverb(0, mediaPlayer.getAudioSessionId());
        presetReverb.setEnabled(true);
        TextView prTitle = new TextView(this);
        prTitle.setText("音场");
        ll.addView(prTitle);
        for (short i = 0; i < equalizer.getNumberOfPresets(); i++) {
            reverbName.add(i);
            reverbVals.add(equalizer.getPresetName(i));
        }

        Spinner sp = new Spinner(this);
        sp.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                reverbVals));
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presetReverb.setPreset(reverbName.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ll.addView(sp);
    }

    private void actionBarSetting() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        //标题
        actionBar.setTitle("Title");
        //副标题
        actionBar.setSubtitle("Subtitle");
        //navigation up 可见
        actionBar.setDisplayHomeAsUpEnabled(true);
        //navigation up 可用
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setHomeAsUpIndicator();
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
            case R.id.actionSet:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (visualizer != null) {
            visualizer.release();
        }
        Log.i("onDestroy", "销毁");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0] == PermissionUtil.RECORD_AUDIO) {
            if (mediaPlayer != null) {
                setupVisualizer();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}