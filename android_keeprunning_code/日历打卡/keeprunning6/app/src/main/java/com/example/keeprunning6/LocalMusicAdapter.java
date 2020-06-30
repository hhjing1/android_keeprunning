package com.example.keeprunning6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LocalMusicAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<LocalMusicBean>list;

    public LocalMusicAdapter(Context context,ArrayList<LocalMusicBean>list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        public TextView music_num,music_song,music_singer,music_album,music_durtion;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_local_music, null);
            viewHolder = new ViewHolder();
            viewHolder.music_num = convertView.findViewById(R.id.item_local_music_num);
            viewHolder.music_song=convertView.findViewById(R.id.item_local_music_song);
            viewHolder.music_singer=convertView.findViewById(R.id.item_local_music_singer);
            viewHolder.music_album=convertView.findViewById(R.id.item_local_music_album);
            viewHolder.music_durtion=convertView.findViewById(R.id.item_local_music_durtion);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        LocalMusicBean bean=list.get(position);
        viewHolder.music_num.setText((position+1)+"");
        viewHolder.music_song.setText(bean.getSong());
        viewHolder.music_singer.setText(bean.getSinger());
        viewHolder.music_album.setText(bean.getAlbum());
        viewHolder.music_durtion.setText(bean.getDuration());
        return convertView;
    }
}
