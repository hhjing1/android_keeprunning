package com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.keeprunning1.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class AlarmFragment extends Fragment {
    private List<Alarm> list;
    private Button addalarm;
    private AlarmOperator alarmOperator;
    private AlarmAdapter alarmAdapter;
    private TextView hour;
    private TextView minute1;
    private EditText content;
    private Button setTime;
    View view;
    String username;
    String hourformat;
    String minuteformat;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hello", "hello");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarm, null);
        username = getArguments().getString("username");
        Log.i("username", username);
        addalarm = view.findViewById(R.id.addalarm);
        list = new ArrayList<>();
        alarmOperator = new AlarmOperator(view.getContext());
        ListView alarmList =view.findViewById(R.id.clock_list);
        init(username);
        alarmAdapter = new AlarmAdapter(view.getContext(), list);
        alarmList.setAdapter(alarmAdapter);
        addalarm.setOnClickListener(l);
        calendar = Calendar.getInstance();
        delete1();
        return view;
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.addalarm:
                    alarm();
                    break;
            }
        }
    };

    public void alarm(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getView().getContext());
        LayoutInflater inflater2 = LayoutInflater.from(getView().getContext());
        View viewDialog2 = inflater2.inflate(R.layout.addalarm, null);
        hour = viewDialog2.findViewById(R.id.hour);
        minute1 = viewDialog2.findViewById(R.id.minute);
        setTime = viewDialog2.findViewById(R.id.set_time);
        addtime();
        content = viewDialog2.findViewById(R.id.content);
        builder2.setView(viewDialog2);
        builder2.setTitle("新闹钟");
        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userhour = hour.getText().toString().trim();
                String userminute = minute1.getText().toString().trim();
                String usercontent = content.getText().toString().trim();
                Alarm bean = new Alarm(username, userhour, userminute, usercontent);
                if(userhour.equals("")||userminute.equals("")){
                    Toast.makeText(view.getContext(), "前选择时间", Toast.LENGTH_SHORT).show();
                }else{
                    alarmOperator.insert(bean);
                    list.add(bean);
                    Toast.makeText(view.getContext(), bean.hour+":"+bean.minute+"添加成功", Toast.LENGTH_SHORT).show();
                    alarmAdapter.notifyDataSetChanged();
                }
            }
        });
        builder2.setNegativeButton("取消", null);
        builder2.create().show();
    }

    public void addtime() {
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mhour = calendar.get(Calendar.HOUR_OF_DAY);
                int mminute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        hourformat = format(hourOfDay);
                        minuteformat =format(minute);
                        Toast.makeText(view.getContext(), "" + hourformat + ":" + minuteformat, Toast.LENGTH_SHORT).show();
                        hour.setText(hourformat);
                        minute1.setText(minuteformat);
                    }
                }, mhour, mminute, true).show();
            }
        });
    }

    public void delete() {
        ListView alarmList = (ListView) view.findViewById(R.id.clock_list);
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("确定删除？");
                builder.setTitle("提示");
                final Alarm[] alarms = {null};
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarms[0] = list.get(position);
                        alarmOperator.deletebyuser(username, alarms[0].hour, alarms[0].minute);
                        list.remove(position);
                        alarmAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return false;
            }
        });
    }
    public void delete1(){
        ListView alarmList = (ListView) view.findViewById(R.id.clock_list);
        alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("确定删除？");
                builder.setTitle("提示");
                builder.setTitle("提示");
                final Alarm[] alarms = {null};
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarms[0] = list.get(position);
                        alarmOperator.deletebyuser(username, alarms[0].hour, alarms[0].minute);
                        list.remove(position);
                        alarmAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
    }
    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    private void init(String username) {
        Cursor cursor = (Cursor) alarmOperator.findall(username);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Alarm bean = new Alarm();
                bean.content = cursor.getString(cursor.getColumnIndex("content"));
                bean.hour = cursor.getString(cursor.getColumnIndex("hour"));
                bean.minute = cursor.getString(cursor.getColumnIndex("minute"));
                bean.username = cursor.getString(cursor.getColumnIndex("username"));
                bean.clockType = cursor.getInt(cursor.getColumnIndex("clockType"));
                Log.d("volley", bean.hour);
                list.add(bean);
            }
            cursor.close();
        }
    }

    //public void updateSwitch(){
//        if(aSwitch.isChecked()==true){
//           alarmOperator.updateswitch(username);
//        }
   // }
}