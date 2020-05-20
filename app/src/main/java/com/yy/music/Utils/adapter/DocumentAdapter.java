package com.yy.music.Utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.music.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentAdapter extends BaseAdapter {
    private Callback callback;
    private Context context;
    private List<String> list;
    Map<Integer, Boolean> map = new HashMap<>();


    public DocumentAdapter(Context context, List<String> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public void setData(List<String> data) {
        map.clear();
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
        this.list = data;
    }

    public Map<Integer, Boolean> getIntemClick() {
        return map;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DocViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new DocViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.docu_item, null);
            viewHolder.cb = convertView.findViewById(R.id.doc_cb);
            viewHolder.iv = convertView.findViewById(R.id.doc_iv);
            viewHolder.tv = convertView.findViewById(R.id.doc_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DocViewHolder) convertView.getTag();
        }
        //Log.i("file","asd   "+list.get(position));
        viewHolder.tv.setText("/" + list.get(position));
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.cb.isChecked()) {
                    Log.i("111", "被选择");
                    map.put(position, true);
                } else {
                    Log.i("111", "未选择");
                    map.put(position, false);
                }
            }
        });
        return convertView;
    }

    class DocViewHolder {
        ImageView iv;
        TextView tv;
        CheckBox cb;
    }

    interface Callback {
        public void onClick(String path);
    }
}
