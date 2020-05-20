package com.yy.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yy.music.Utils.adapter.ListViewAdapter;
import com.yy.music.base.BaseFragment;
import com.yy.music.data.GlobalVariable;
import com.yy.music.databinding.FragmentLoaclBinding;
import com.yy.music.service.MediaService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class LocalFragment extends BaseFragment {
    private FragmentLoaclBinding binding;
    private String path = Environment.getExternalStorageDirectory().getPath();
    private static List<Map<String, String>> list;
    private static ListViewAdapter baseAdapter;
    private MyBroadcastReceiver receiver;
    private int current;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentLoaclBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        //获取数据
        list = GlobalVariable.getInstance().getList();
        //
        baseAdapter = new ListViewAdapter(getContext(), list);
        binding.listView.setAdapter(baseAdapter);

        //注册广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalVariable.getInstance().SEND_LOCAL_DATA);
        Objects.requireNonNull(getContext()).registerReceiver(receiver, filter);

        binding.dingWei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCurrent();
            }
        });
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveCurrent(position);
                baseAdapter.setSelect(position);
                String path = list.get(position).get("filePath");
                //String path = Environment.getExternalStorageDirectory().getPath()+"/Android/卡农.mid";
                Intent intent = new Intent(getContext(), MediaService.class);
                intent.putExtra("path", path);
                Objects.requireNonNull(getActivity()).startService(intent);
                Log.i("listView", "点击");
            }
        });

        readCurrent();
    }

    @Override
    protected void initData() {

    }


    private void readCurrent() {
        SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        current = sp.getInt("music", 0);
        baseAdapter.setSelect(current);
        binding.listView.setSelectionFromTop(current, 200);
    }

    private void saveCurrent(int current) {
        SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("music", current);
        editor.apply();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean update_ = intent.getBooleanExtra("update_", false);
            boolean update = intent.getBooleanExtra("update", false);
            if (update) {
                list = GlobalVariable.getInstance().getList();
                baseAdapter.setData(list);
                baseAdapter.notifyDataSetChanged();
            }
            if (update_) {
                SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("data", MODE_PRIVATE);
                current = sp.getInt("music", 0);
                baseAdapter.setSelect(current);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getContext()).unregisterReceiver(receiver);
    }

}
