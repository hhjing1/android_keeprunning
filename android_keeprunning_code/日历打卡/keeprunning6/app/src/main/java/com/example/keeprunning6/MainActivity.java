package com.example.keeprunning6;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView lastIv,playIv,nextIv,pauseIv;
    //音乐列表试图
    private ListView listView;
    //音乐列表
    private ArrayList<LocalMusicBean> list=new ArrayList<>();
    //当前播放的音乐以及歌手
    private TextView now_music_song,now_music_singer;

    private LocalMusicAdapter adapter;
    private int position;
    // 在播放音乐的位置
    int currentPlayPosition=-1;
    //记录暂停音乐时进度条的位置
    int currentPausePositionSong=0;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.music_list);
        now_music_song=findViewById(R.id.local_music_botton_tv_song);
        now_music_singer=findViewById(R.id.local_music_bottom_tv_singer);
        playIv=findViewById(R.id.local_music_bottom_iv_play);
        lastIv=findViewById(R.id.local_music_bottom_iv_last);
        nextIv=findViewById(R.id.local_music_bottom_iv_next);

        mediaPlayer=new MediaPlayer();
        //自定义数据

        adapter=new LocalMusicAdapter(MainActivity.this,list);
        listView.setAdapter(adapter);

        //加载本地数据源
        localMusicData();
        //设置每一项的点击事件
        playIv.setOnClickListener(l);
        lastIv.setOnClickListener(l);
        nextIv.setOnClickListener(l);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //上面的position参数是歌曲在列表中的索引
                currentPlayPosition=position;
                LocalMusicBean musicBean=list.get(position);
                playMusicPosition(musicBean);
            }
        });
    }

    //根据传入对象播放音乐
    public void playMusicPosition(LocalMusicBean musicBean) {
        //底部显示的歌手名和歌曲名
        now_music_singer.setText(musicBean.getSinger());
        now_music_song.setText(musicBean.getSong());
        stopMusic();
        //重制多媒体音乐播放器
        mediaPlayer.reset();
        //改变地址，重新设置路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放音乐的函数
    /*根据currentPausePositionSong的数值判断
    * 1.从暂停到播放
    * 2.从停止到播放
    * */
    private void playMusic() {
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            //判断是否暂停，以判断音乐是否从头播放
            if (currentPausePositionSong==0) {
                //从头开始播放
                try {
                    //因为之前重制了音乐，所以现在要做准备
                    mediaPlayer.prepare();
                    //准别完之后开始播放
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //从暂停开始播放
                //将进度条挪动到指定的位置上
                mediaPlayer.seekTo(currentPausePositionSong);
                //开始播放
                mediaPlayer.start();

            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }

    }

    //暂停音乐的函数
    private void pauseMusic() {
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionSong=mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    //停止音乐的函数
    private void stopMusic(){
        if(mediaPlayer!=null){
            //一暂停，改变currentPausePositionSong的数值为0
            currentPausePositionSong=0;
            //先暂停音乐
            mediaPlayer.pause();
            //将暂停的音乐的播放条回到最初
            mediaPlayer.seekTo(0);
            //再停止音乐的播放
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);

        }
    }

    protected void onDestroy(){
        super.onDestroy();
        stopMusic();
    }


    private void localMusicData(){
        //加载本地存储当中的音乐mp3文件到集合当中

        //1.获取ContentResolver对象
        ContentResolver resolver=getContentResolver();
        //2.获取本地音乐存储的Uri地址
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //3.开始查询地址
        Cursor cursor=resolver.query(uri,null,null,null,null);
        //4.遍历Cursor
        int id=0;
        while(cursor.moveToNext()){
            String song=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            //歌曲编号变成String类型
            String sid=String.valueOf(id);
            String path=cursor.getString(cursor.getColumnIndex((MediaStore.Audio.Media.DATA)));
           // String duration=cursor.getString(cursor.getColumnIndex((MediaStore.Audio.Media.DURATION)));
           int duration=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
            String time = formatTime(duration);

            //将一行当中的数据封装到对象当中
            LocalMusicBean bean=new LocalMusicBean(sid,song,singer,album,time,path);
            list.add(bean);

        }
        //数据变化，提示适配器更新
        adapter.notifyDataSetChanged();

    }

    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }



    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.local_music_bottom_iv_last:
                    if (currentPlayPosition==0) {
                        Toast.makeText(MainActivity.this,"这是第一首歌曲，没有上一曲了",Toast.LENGTH_SHORT);
                        return;
                    }
                    currentPlayPosition=currentPlayPosition-1;
                    LocalMusicBean lastBean=list.get(currentPlayPosition);
                    playMusicPosition(lastBean);
                    break;
                case R.id.local_music_bottom_iv_play:
                    if (currentPlayPosition==-1) {
                        //没有选中要播放的音乐
                        // Toast.makeText(this,"请选择要播放的音乐",Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this,"请选择要播放的音乐",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mediaPlayer.isPlaying()) {
                        //此时处于播放状态，需要暂停音乐
                        pauseMusic();
                    }else{
                        //此时没有播放音乐，点击开始播放音乐
                        playMusic();
                    }
                    break;
                case R.id.local_music_bottom_iv_next:
                    if (currentPlayPosition==list.size()-1) {
                        Toast.makeText(MainActivity.this,"这是最后一首歌曲，没有下一曲了",Toast.LENGTH_SHORT);
                        return;
                    }
                    currentPlayPosition=currentPlayPosition+1;
                    LocalMusicBean nextBean=list.get(currentPlayPosition);
                    playMusicPosition(nextBean);
                    break;
            }

        }
    };

}
