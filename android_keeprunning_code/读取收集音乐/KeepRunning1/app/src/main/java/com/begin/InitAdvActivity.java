package com.begin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.keeprunning1.R;
import com.login.LoginActivity;

import java.util.Random;

public class InitAdvActivity extends BaseActivity {
    private int[] picsLayout = {R.layout.begin_one, R.layout.begin_two,
            R.layout.begin_three, R.layout.begin_four, R.layout.begin_five};
    private int i;
    private int count = 5;
    private Button mBtnSkip;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mBtnSkip.setText("跳过 (" + getCount() + ")");
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
    private LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Random r = new Random();
        i = r.nextInt(5);
        Log.i("test", "随机数是:" + i);
        if (i < 5) {
            //随机概率会出现广告页
            setContentView(R.layout.activity_adv);
            initView2();
        } else {
            startActivity(new Intent(InitAdvActivity.this, LoginActivity.class));
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    private void initView2() {
        linearLayout = findViewById(R.id.advLine);
        View view = View.inflate(InitAdvActivity.this, picsLayout[i], null);
        linearLayout.addView(view);
        mBtnSkip = view.findViewById(R.id.btn_skip);
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InitAdvActivity.this, LoginActivity.class));
                handler.removeMessages(0);
                finish();
            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    public int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }
}
