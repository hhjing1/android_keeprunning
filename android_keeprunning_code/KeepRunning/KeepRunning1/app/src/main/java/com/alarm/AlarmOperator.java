package com.alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmOperator {
    private AlarmDBHelper alarmDBHelper;
    private SQLiteDatabase db;
    public AlarmOperator(Context context){
        alarmDBHelper=new AlarmDBHelper(context,"alarm",null,1);
        db=alarmDBHelper.getWritableDatabase();
    }
    //添加闹钟
    public void insert(Alarm bean){
        db.execSQL("insert into useralarm (username,hour,minute,content) values(?,?,?,?)",new Object[]{bean.username,bean.hour,bean.minute,bean.content});
    }

    public Cursor findall(String username){
        Cursor c=db.rawQuery("select * from useralarm where username = ? ",new String[]{username});
        return c;
    }
    public void deletebyuser(String username,String hour,String minute){
        db.execSQL("delete from useralarm where username=? and hour=? and minute=?", new String[]{username,hour,minute});
    }
    public Alarm isExit(String name) {
        Alarm bean = null;
        Cursor c = db.rawQuery("select * from Users where username=?", new String[]{name});
        while (c.moveToNext()) {
            bean = new Alarm();
            bean.setUsername(c.getString(c.getColumnIndex("username")));
            bean.setHour(c.getString(c.getColumnIndex("hour")));
            bean.setMinute(c.getString(c.getColumnIndex("minute")));
            bean.setContent(c.getString(c.getColumnIndex("content")));
            bean.setClockType(c.getInt(c.getColumnIndex("clockType")));
        }
        c.close();
        return bean;
    }

    public void updateswitch(String username,String hour,String minute){
        db.execSQL("update useralarm set clockType = ? where username=? and hour=? and minute=?", new Object[]{1, username,hour,minute});
    }
    public void updateswitch1(String username,String hour,String minute){
        db.execSQL("update useralarm set clockType = ? where username = ?  and hour=? and minute=?", new Object[]{0, username,hour,minute});
    }
}
