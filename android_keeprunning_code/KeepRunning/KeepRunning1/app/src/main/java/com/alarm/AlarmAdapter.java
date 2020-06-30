package com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.keeprunning1.R;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


public class AlarmAdapter extends BaseAdapter {
    private List<Alarm> mlist;
    private Context mContext;
    private LayoutInflater mlayoutInflater;
    private AlarmOperator malarmOperator;
    Calendar calendar;
    private int randNum;

    public AlarmAdapter(Context context, List<Alarm> list) {
        mContext = context;
        mlist = list;
        mlayoutInflater = LayoutInflater.from(context);
        malarmOperator = new AlarmOperator(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mlayoutInflater.inflate(R.layout.alarmitem, null);
            viewHolder.mHour = convertView.findViewById(R.id.hour);
            viewHolder.mMinute = convertView.findViewById(R.id.minute);
            viewHolder.mContent = convertView.findViewById(R.id.content_item);
            viewHolder.mNet=convertView.findViewById(R.id.net);
            viewHolder.mClockType = convertView.findViewById(R.id.switch_control);
            viewHolder.todetail = convertView.findViewById(R.id.todetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Alarm bean = mlist.get(position);
        final String name = bean.getUsername();
        final String hour = bean.getHour();
        final String minute = bean.getMinute();
        Log.i("name", name);
        int clockType = bean.getClockType();
        Log.d("volley", String.valueOf(clockType));
        if (clockType == 1) {
            viewHolder.mClockType.setChecked(true);
        } else if (clockType == 0) {
            viewHolder.mClockType.setChecked(false);
            viewHolder.mHour.setTextColor(mContext.getResources().getColor(R.color.colorGray));
            viewHolder.mMinute.setTextColor(mContext.getResources().getColor(R.color.colorGray));
            viewHolder.mNet.setTextColor(mContext.getResources().getColor(R.color.colorGray));
            viewHolder.mContent.setTextColor(mContext.getResources().getColor(R.color.colorGray));
        }

        viewHolder.todetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定删除？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        malarmOperator.deletebyuser(name, hour, minute);
                        //notifyDataSetChanged();
                        AlarmManager am;
                        Intent intent1 = new Intent(mContext, CallAlarm.class);
                        PendingIntent sender1=PendingIntent.getBroadcast(
                                mContext,0, intent1, 0);
                        am =(AlarmManager)mContext.getSystemService(ALARM_SERVICE);
                        am.cancel(sender1);
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
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
        viewHolder.mClockType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    malarmOperator.updateswitch(name, hour, minute);
//                    viewHolder.mClockType.setChecked(true);
                    Toast.makeText(mContext, "开启闹钟", Toast.LENGTH_SHORT).show();
                    viewHolder.mHour.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                    viewHolder.mMinute.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                    viewHolder.mNet.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                    viewHolder.mContent.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                    Random rand=new Random();
                    randNum=rand.nextInt(1000000000);
                    Log.i("randNum",String.valueOf(randNum));

                    Intent intent = new Intent(mContext, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(
                            mContext, randNum, intent, 0);
                    AlarmManager am;
                    //使用闹钟服务
                    am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bean.getHour()));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(bean.getMinute()));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Log.e("TAG", calendar.getTimeInMillis() + "");
                    Log.e("TAG", System.currentTimeMillis() + "");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (System.currentTimeMillis() > calendar.getTimeInMillis() + 40000) {
                            //加24小时
                            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 86400000, sender);
                        } else {
                            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                    }
                } else if (!isChecked) {
                    malarmOperator.updateswitch1(name, hour, minute);
                    viewHolder.mHour.setTextColor(mContext.getResources().getColor(R.color.colorGray));
                    viewHolder.mMinute.setTextColor(mContext.getResources().getColor(R.color.colorGray));
                    viewHolder.mNet.setTextColor(mContext.getResources().getColor(R.color.colorGray));
                    viewHolder.mContent.setTextColor(mContext.getResources().getColor(R.color.colorGray));
                    Intent intent = new Intent(mContext, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(
                            mContext, 0, intent, 0);
                    AlarmManager am;
                    am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                    am.cancel(sender);
                    Toast.makeText(mContext, "关闭闹钟", Toast.LENGTH_SHORT).show();

                }
            }
        });
        viewHolder.mContent.setText(bean.content);
        viewHolder.mHour.setText(bean.hour+"");
        viewHolder.mMinute.setText(bean.minute+"");
        return convertView;
    }
    public class ViewHolder {
        public TextView mHour;
        public TextView mMinute;
        public TextView mContent;
        public TextView mNet;
        Switch mClockType;
        LinearLayout todetail;
        //public CheckBox mCheckBox;
    }
}
