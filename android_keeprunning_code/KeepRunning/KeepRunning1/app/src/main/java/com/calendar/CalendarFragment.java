package com.calendar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.baidu.mapapi.SDKInitializer;
import com.bean.location;
import com.calendar.view.MonPickerDialog;
import com.calendar.view.SignView;
import com.example.keeprunning1.R;

import com.maplocation.DynamicDemo;
import com.weather.WeatherActivity;


import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class CalendarFragment extends Fragment {
    private static final String TAG = "MainActivity--->>>";
    private SignView signDate;
    private TextView tvYear;
    private Button sign,sign2;
    private Button tianqi;

    View view;
    private String username;
    private SDKReceiver mReceiver;
    location location;
    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        //onreceieve方法中就是我们想要广播接收器收到广播之后需要处理的操作
        //广播专用的方法判断当前的状态
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            String msg = intent.getStringExtra("data");
            if("refresh".equals(msg)) {
                refresh();
                location = (location) intent.getSerializableExtra("message");
            }
            getquanxian();

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
               // Toast.makeText(getActivity(),"apikey验证失败，地图功能无法正常使用",Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                Toast.makeText(getActivity(),"apikey验证成功",Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
               // Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ////在使用SDK各组件之前初始化context信息，传入ApplicationContext
        ////注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplication());
        view = inflater.inflate(R.layout.mycalendar,null);
        Intent intent = getActivity().getIntent();//获取Intent对象
        username = intent.getStringExtra("username");
        //username = getArguments().getString("username");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("filter",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.commit();
        initView();
        // apikey的授权需要一定的时间，在授权成功之前地图相关操作会出现异常；apikey授权成功后会发送广播通知，我们这里注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();//创建实例化对象。这样就可以使用广播事件
        //registerReceiver和unregisterReceiver的调用者必须一致
        /**
         *动态注册的步骤：
         在相关的activity文件中new一个刚才我们定义的广播类
         new一个intentFilter类，调用其的setAction方法，参数中传入相关值的action
         调用context.registerReceiver方法进行注册，方法的第一个参数为广播类，第二个则是intentFilter类

         */
        getActivity().registerReceiver(mReceiver, iFilter);//接受广播
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
        return view;
    }
    private void initView() {
        sign = view.findViewById(R.id.btn_sign);
        sign2=view.findViewById(R.id.btn_sign2);
        signDate = view.findViewById(R.id.signDate);
        tvYear =signDate.findViewById(R.id.tvYear);
        tianqi=(Button)view.findViewById(R.id.btn_tianqi);
        tianqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        signDate.init();

        if(signDate.isSign()) {

            sign2.setVisibility(View.VISIBLE);
            sign2.setEnabled(true);
            sign.setBackgroundColor(Color.GRAY);
            sign.setText("今日已打卡");
            sign.setClickable(false);
        }else {

            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handle();
                    Intent intent = new Intent(getActivity(), DynamicDemo.class);
                    intent.putExtra("username",username);
                    startActivity(intent);

                    sign2.setVisibility(View.VISIBLE);
                    sign2.setEnabled(true);
                }
            });

        }
        sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DynamicDemo.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonPicker();
            }
        });
    }
    private void handle() {
        signDate.signIn(new DateAdapter.OnSignListener() {
            @Override
            public void OnSignedSucceed() {
                showToast("打卡成功");
                sign.setBackgroundColor(Color.GRAY);
                sign.setText("今日已打卡");
                sign.setClickable(false);
            }
            @Override
            public void OnSignedFail() {
                showToast("打卡失败");
            }
        });

    }

    private void showMonPicker() {
        final Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(DateUtil.strToDate("yyyy-MM", tvYear.getText().toString()));
        MonPickerDialog dialog = new MonPickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                localCalendar.set(Calendar.YEAR, year);
                localCalendar.set(Calendar.MONTH, monthOfYear);
                localCalendar.set(Calendar.DATE, dayOfMonth);
            }
        }, localCalendar.get(Calendar.YEAR), localCalendar.get(Calendar.MONTH),localCalendar.get(Calendar.DATE));

        dialog.show();

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvYear.setText(DateUtil.calendarToDateTime(localCalendar, "yyyy-MM"));
                refresh(localCalendar.get(Calendar.YEAR),localCalendar.get(Calendar.MONTH)+1);
            }
        });
    }



    private void refresh(int year,int month) {
        if(year!=DateUtil.year || month!=DateUtil.month){
            signDate.init(year,month);
            sign.setBackgroundColor(getResources().getColor(R.color.sky_blue));
            sign.setText("返回当前日期");
            sign.setClickable(true);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initView();
                }
            });

        }

    }
    //刷新用的
    private void refresh() {
        initView();
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //registerReceiver和unregisterReceiver的调用者必须一致
        getActivity().unregisterReceiver(mReceiver);
    }
    //刷新作用
    @Override
    public void onResume() {
        super.onResume();
        initView();
}

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        initView();
    }
    private void getquanxian()
    {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else {
            Toast.makeText(getActivity(), "已开启定位权限", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200://刚才的识别码
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//用户同意权限,执行我们的操作
                }else{//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(getActivity(),"未开启定位权限,请手动到设置去开启权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }

}