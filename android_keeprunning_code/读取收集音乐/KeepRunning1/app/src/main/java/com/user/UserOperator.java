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
        db.execSQL("insert into Users(username,userpsw,userpicture,usercheck)values(?,?,?,?)", new Object[]{bean.username, bean.userpsw, bean.userpicture, bean.usercheck});
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
        }
        c.close();
        return bean;
    }

    //更新是否记住密码
    public void isRemPsw(User beans) {
        db.execSQL("update Users set usercheck = ? where username = ?", new Object[]{beans.usercheck, beans.username});
    }

}
