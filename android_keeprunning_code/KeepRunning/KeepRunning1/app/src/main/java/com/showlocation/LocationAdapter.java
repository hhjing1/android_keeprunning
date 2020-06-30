package com.showlocation;

import android.widget.BaseAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.bean.*;
import com.example.keeprunning1.R;

import java.util.List;
public class LocationAdapter extends BaseAdapter {
    private List<location> mlist;
    private Context mContext;
    private LayoutInflater mlayoutInflater;
    public LocationAdapter(Context context, List<location> list){
        mContext=context;
        if (mContext == null) return;
        mlist=list;
        mlayoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {//返回一共有多少条记录
        System.out.println("这个list的长度是"+mlist.size());
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {//返回当前的item对象
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {//返回当前item的id
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=mlayoutInflater.inflate(R.layout.list_viewlayout,null);
            viewHolder.distancetxt=convertView.findViewById(R.id.distancetxt);
            viewHolder.datatxt=convertView.findViewById(R.id.datatxt);
            viewHolder.enerytxt=convertView.findViewById(R.id.enerytxt);
            // viewHolder.eventtxt=convertView.findViewById(R.id.eventtxt);
            // viewHolder.choicetxt=convertView.findViewById(R.id.choicetxt);
            // viewHolder.timetxt=convertView.findViewById(R.id.timetxt);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        location message=mlist.get(position);
        viewHolder.distancetxt.setText(message.distance);
        viewHolder.datatxt.setText(message.date);
        viewHolder.enerytxt.setText(message.energy);
        viewHolder=new ViewHolder();
        viewHolder.listwen=convertView.findViewById(R.id.list_view);
        if (Double.parseDouble(message.distance)>2.0) {
            convertView.setBackgroundColor(Color.parseColor("#9370DB"));//背景色
        } else {
            convertView.setBackgroundColor(Color.parseColor("#9370DB"));//背景色
        }
        //   viewHolder.eventtxt.setText(message.userevent);
        //  viewHolder.timetxt.setText(message.usertime);
        //   viewHolder.choicetxt.setText(message.userchoice);

        return convertView;
    }

    private static class ViewHolder{//该类中包括item文件（activity_news_list_view）中所有需要显示内容的组件
        public TextView distancetxt;
        public TextView datatxt;
        public TextView enerytxt;
        public ListView listwen;

    }
}
