package com.begin;


import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    Intent it = new Intent(getApplicationContext(), InitAdvActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
