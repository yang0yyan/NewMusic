package com.yy.music.Utils.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.music.R;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {
    private int curPos=0;
    private Context context;
    List<Map<String,String>> list;
    public ListViewAdapter(Context context , List<Map<String,String>> list) {
        super();
        this.context = context;
        this.list = list;
    }
    public void setData(List<Map<String,String>> list){
        this.list = list;
    }
    public void setSelect(int current){
        curPos = current;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(list.size()<1)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.local_item,null);
            viewHolder.select = convertView.findViewById(R.id.select_color);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.msg = convertView.findViewById(R.id.msg);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).get("name"));
        viewHolder.msg.setText(list.get(position).get("artist"));
        if(curPos == position)
            viewHolder.select.setBackgroundResource(R.color.colorGreen);
        else
            viewHolder.select.setBackgroundResource(R.color.colorWhite);
        return convertView;
    }
    static class ViewHolder{
        ImageView iv;
        TextView title;
        TextView msg;
        TextView select;
    }
}
