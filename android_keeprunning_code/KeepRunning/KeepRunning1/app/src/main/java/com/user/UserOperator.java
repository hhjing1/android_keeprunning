package com.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserOperator {

    private UserDBHelper UserDBHelper;
    private SQLiteDatabase db;

    public UserOperator(Context context) {
        UserDBHelper = new UserDBHelper(context, "db_user", null, 1);
        db = UserDBHelper.getWritableDatabase();
    }

    //增加用户
    public void addUser(User bean) {
        db.execSQL("insert into Users(username,userpsw,userpicture,usercheck,nicheng,qianming,health)values(?,?,?,?,?,?,?)", new Object[]{bean.username, bean.userpsw, bean.userpicture, bean.usercheck, "", "",""});
    }
    //修改健康指数
    public void add(User bean) {
        db.execSQL("update Users set health= ? where username = ?", new Object[]{bean.health, bean.username});
    }
    //判断用户是否存在
    public User isExit(String name) {
        User bean = null;
        Cursor c = db.rawQuery("select * from Users where username=?", new String[]{name});
        while (c.moveToNext()) {
            bean = new User();
            bean.setUsername(c.getString(c.getColumnIndex("username")));
            bean.setUserpsw(c.getString(c.getColumnIndex("userpsw")));
            bean.setUsercheck(c.getString(c.getColumnIndex("usercheck")));
            bean.setUserpicture(c.getString(c.getColumnIndex("userpicture")));
            bean.setNicheng(c.getString(c.getColumnIndex("nicheng")));
            bean.setQianming(c.getString(c.getColumnIndex("qianming")));
            bean.setHealth(c.getString(c.getColumnIndex("health")));
        }
        c.close();
        return bean;
    }

    //更新是否记住密码
    public void isRemPsw(User beans) {
        db.execSQL("update Users set usercheck = ? where username = ?", new Object[]{beans.usercheck, beans.username});
    }

    //用户注销
    public void usercancel(String name) {
        db.execSQL("delete from Users  where username = ?", new Object[]{name});
    }

    //修改信息
    public void updateuser(User beans) {
        db.execSQL("update Users set userpsw= ?, nicheng= ?, qianming= ? where username = ?", new Object[]{beans.userpsw,beans.nicheng, beans.qianming, beans.username});
    }
    //修改信息
    public void updateuser1(User beans) {
        db.execSQL("update Users set userpsw= ?, userpicture= ?,nicheng= ?, qianming= ? where username = ?", new Object[]{beans.userpsw,beans.userpicture,beans.nicheng, beans.qianming, beans.username});
    }

}
