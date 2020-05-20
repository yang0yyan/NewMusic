package com.yy.music.Utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.music.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoaclDocAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list = new ArrayList<>();

    public LoaclDocAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<Map<String, String>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.local_doc_item, null);
            viewHolder.iv = convertView.findViewById(R.id.local_doc_item_iv);
            viewHolder.tv = convertView.findViewById(R.id.local_doc_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(list.get(position).get("name"));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
