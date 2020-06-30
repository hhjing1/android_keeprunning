package com.showlocation;

import androidx.fragment.app.Fragment;
import com.bean.*;
import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.DBOpenMessageUser.*;
import com.example.keeprunning1.R;
import com.showlocation.StaticDemo2;

import java.util.ArrayList;
import java.util.List;

public class showlocationFragment extends Fragment {
    private DBOpenMessageUserlocation dbOpenMessage;
    private String username;
    private ListView listview;
    private List<location> alllistmessage = new ArrayList<location>();
    private LocationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_showlocation, container, false);
        listview = (ListView) view.findViewById(R.id.list_view);
        init();
        return view;
    }
    private void init()
    {
        dbOpenMessage = new DBOpenMessageUserlocation(getActivity(), "db_location", null, 1);

        Intent intent = getActivity().getIntent();//获取Intent对象
        username = intent.getStringExtra("username");
        //username = getArguments().getString("username");
        alllistmessage.clear();
        getMessage1(username);
        adapter = new LocationAdapter(getActivity(), alllistmessage);
        //listview.setAdapter(null);
        //adapter.notifyDataSetChanged();

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //   Intent intent=new Intent(getActivity(),AllMessage.class);
                // intent.putExtra("username",username2);
                //startActivity(intent);
                location message = (location) parent.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setClass(getActivity(), StaticDemo2.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", message);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /*  private void xianshixinxi()
      {
          Cursor cursor1=dbOpenMessage.getReadableDatabase().query("db_wen2",null,"username=?",new String[]{username},null,null,null);
          ArrayList<Map<String,String>> resultlist=new ArrayList<Map<String,String >>();
      }*/
    private void getMessage1(String username) {
        Cursor cursor = dbOpenMessage.getAllLocation(username);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                location message2 = new location();
                message2.distance = cursor.getString(cursor.getColumnIndex("distance"));
                message2.energy = cursor.getString(cursor.getColumnIndex("energy"));
                message2.date = cursor.getString(cursor.getColumnIndex("date"));
                message2.points = cursor.getString(cursor.getColumnIndex("points"));
                message2.speed = cursor.getString(cursor.getColumnIndex("speed"));
                message2.time = cursor.getString(cursor.getColumnIndex("time"));
                message2.username = cursor.getString(cursor.getColumnIndex("username"));
                message2.id = cursor.getInt(cursor.getColumnIndex("_id"));
                alllistmessage.add(message2);
            }
        }
    }

    //实现长按删除listview里的item事件
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListView listView = (ListView) getActivity().findViewById(R.id.list_view);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final location[] account = {null};
                account[0] = alllistmessage.get(position);
                builder.setTitle("是否确定删除此运动？？？");
                builder.setMessage("记录内容如下：\n"+"用户： "+account[0].username+"\n运动日期： "+account[0].date+"\n运动距离： "+account[0].distance+"\n消耗能量： "+account[0].energy);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbOpenMessage.deletebynameanddistance(account[0].username,account[0].date);
                        alllistmessage.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(view.getContext(), "删除该事件成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });

    }
    //刷新作用
    @Override
    public void onResume() {
        super.onResume();
       init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        init();
    }

}
