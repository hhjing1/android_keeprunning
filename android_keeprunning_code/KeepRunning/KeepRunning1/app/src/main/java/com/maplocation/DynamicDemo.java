package com.maplocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.DBOpenMessageUser.DBOpenMessageUserlocation;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.DBOpenMessageUser.*;
import com.bean.*;
import com.calendar.CalendarFragment;
import com.example.keeprunning1.R;
import com.maplocation.StaticDemo;
import com.showlocation.StaticDemo2;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DynamicDemo extends Activity {
    private DBOpenMessageUserlocation dbOpenMessageUserlocation;
//    Gson gson=new Gson();
    private String dingweiqueding="";
    location location;
//计算距离
double power=0;
double distribution=0;
double sportMile=0;
DecimalFormat decimalFormat;
    private String username;
    TextView tvMileage,tvSpeed;
    double distance=0;//路程
    long seconds = 0;//秒数(时间)
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private TextView tvcalor;
    //秒表
    private EditText edtSetTime;
    private int startTime = 0;
    private boolean PAUSE = false;
    //从开始到暂停的时间差
    private long rangeTime;
    private Chronometer chronometer;
    //秒表结束
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker, othersCurrentMarker;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private MyLocationData locData;
    float mCurrentZoom = 18.0f;//默认地图缩放比例值
    MapStatus.Builder builder;

    private SensorManager mSensorManager;
    double degree = 0;
    // UI相关
    OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    TextView startguiji;
    ToggleButton togglebtn = null;
    boolean isFirstLoc = true;// 是否首次定位

    //轨迹相关
    boolean guiji = false;//运行轨迹
    boolean isFirstGuiji = true;
    //起点图标
    BitmapDescriptor startBD;
    //终点图标
    BitmapDescriptor finishBD;
    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    Polyline mPolyline;//运动轨迹图层
    LatLng last = new LatLng(0, 0);//上一个定位点
    //MapStatus.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);
        Intent intent = getIntent();
         username = intent.getStringExtra("username");
         System.out.println(username);
        //数据库
        dbOpenMessageUserlocation=new DBOpenMessageUserlocation(DynamicDemo.this,"db_location",null,1);
        judgePermission();
        requestLocButton = (Button) findViewById(R.id.button1);
        mCurrentMode = LocationMode.FOLLOWING;
        requestLocButton.setText("跟随");
        OnClickListener btnClickListener = new OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("跟随");
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        togglebtn = (ToggleButton) findViewById(R.id.togglebutton);
        togglebtn
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            // 热力地图
                            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                            mBaiduMap.setBaiduHeatMapEnabled(true);
                        } else {
                            // 普通地图
                            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                            mBaiduMap.setBaiduHeatMapEnabled(false);

                        }

                    }
                });

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        //指南针
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor magenticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(listener, magenticSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        //添加他人位置
        Resources r = this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.icon_geo);
        addOthersLocation(36.6736687292, 117.1459498599, bmp);
//秒表的设置
        chronometer = (Chronometer) findViewById(R.id.cm_passtime);
        edtSetTime=(EditText) findViewById(R.id.setTimeEdit);
        chronometer.setFormat("00:%s");
        //轨迹
        startguiji = (TextView) findViewById(R.id.startguiji);
        startguiji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!guiji) {//开始
                    guiji = true;
                    startguiji.setText("停止运动");
                    setLeftDrawable1(startguiji, 2);
                    /**
                     * 执行秒表开启工作
                     * */
                    rangeTime = 0;
                    String ss = edtSetTime.getText().toString();
                    if (!(ss.equals("") && ss != null)) {
                        startTime = (Integer.parseInt(edtSetTime.getText().toString())) * 60;
                    }
                    // 设置开始计时时间
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    // 开始记时
                    chronometer.start();
                    PAUSE = false;
                } else {//停止
                    guiji = false;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    Date date1 = new Date(System.currentTimeMillis());
                    String date = simpleDateFormat.format(date1);
                    rangeTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    String wen = dingweiqueding;

//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您rrrrr"+we33n,Toast.LENGTH_SHORT).show();

                    startguiji.setText("开始运动");
                    // System.out.println("于东速度，能量，"+location.getSpeed()+location.getEnergy());
//                    if (mLocClient != null && mLocClient.isStarted()) {
                    //mLocClient.stop();
                    if (isFirstGuiji) {
                        points.clear();
                        last = new LatLng(0, 0);
                        return;
                    }
                    dingweiqueding = dingweiqueding.substring(0, dingweiqueding.length() - 1);
                    MarkerOptions oFinish = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oFinish.position(points.get(points.size() - 1));
                    oFinish.icon(finishBD);// 设置覆盖物图片
                    mBaiduMap.addOverlay(oFinish); // 在地图上添加此图层
                    //复位
                    points.clear();
                    last = new LatLng(0, 0);
                    isFirstGuiji = true;
//                    }
                    setLeftDrawable1(startguiji, 1);
                    rangeTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    chronometer.stop();

                    double panduanyixia = (double) distribution;
                    if (panduanyixia > 0.01) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("数据保存成功！");
                        builder.setMessage("2秒后自动关闭！");
                        builder.setCancelable(true);
                        final AlertDialog dlg = builder.create();
                        dlg.show();
                        final Timer t = new Timer();
                        location = new location();
                        location.setDate(date);
                        location.setDistance(String.valueOf(sportMile));
                        location.setEnergy(String.valueOf(power));
                        location.setPoints(wen);
                        location.setSpeed(String.valueOf(distribution));
                        location.setUsername(username);
                        location.setTime(String.valueOf(rangeTime));
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您"+date,Toast.LENGTH_SHORT).show();
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您1"+String.valueOf(distribution),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您2"+String.valueOf(power),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您3"+String.valueOf(sportMile),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您4"+String.valueOf(rangeTime),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(DynamicDemo.this,"成熟度你竭诚为您5"+username,Toast.LENGTH_SHORT).show();

                        long we33n = dbOpenMessageUserlocation.insertlocation(dbOpenMessageUserlocation.getReadableDatabase(), location);
                        Toast.makeText(DynamicDemo.this, "运行数据保存成功", Toast.LENGTH_SHORT).show();
                        t.schedule(new TimerTask() {
                            public void run() {
                                Intent intent = new Intent();
                                //Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                intent.setClass(DynamicDemo.this, StaticDemo.class);
                                // intent.putExtra("data","refresh");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("message", location);
                                intent.putExtras(bundle);

//                            LocalBroadcastManager.getInstance(DynamicDemo.this).sendBroadcast(intent);
//                            sendBroadcast(intent);
                                finish();
                                DynamicDemo.this.finish();

                                dlg.dismiss();
                                t.cancel();
                                startActivity(intent);
                            }
                        }, 2000);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("运动距离太短，数据保存失败！");
                        builder.setMessage("2秒后自动关闭！");
                        builder.setCancelable(true);
                        final AlertDialog dlg = builder.create();
                        dlg.show();
                        final Timer t = new Timer();
                        Toast.makeText(DynamicDemo.this, "运行距离太短，数据不保存", Toast.LENGTH_SHORT).show();
                        t.schedule(new TimerTask() {
                            public void run() {
                                //Intent intent = new Intent();
                                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                //intent.setClass(DynamicDemo.this, StaticDemo.class);
                                intent.putExtra("data", "refresh");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("message", location);
                                intent.putExtras(bundle);

                                LocalBroadcastManager.getInstance(DynamicDemo.this).sendBroadcast(intent);
                                sendBroadcast(intent);
                                finish();
                                DynamicDemo.this.finish();

                                dlg.dismiss();
                                t.cancel();
                                //startActivity(intent);
                            }
                        }, 2000);
                    }
                }
            }
        });
        startBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_startpoint);
        finishBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_finishpoint);
        //关于计时器的操作;
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // 如果开始计时到现在超过了startime秒
                if (SystemClock.elapsedRealtime() - chronometer.getBase() > startTime * 1000) {

                    // 给用户提示
                    showDialog();
                    startTime=1000000;
                }

            }
        });
    }
    //关于秒表的方法
    //时间计时器的提醒操作
    protected void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("恭喜您").setMessage("已经达到运动目标时间")
                .setPositiveButton("继续运动", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//设计运动是的图片
public void setLeftDrawable1(TextView textView, int draw) {
    Drawable drawable;
    if(draw==1)
    {
        drawable = getResources().getDrawable(R.mipmap.run_mode);
    }
    else
    {
        drawable = getResources().getDrawable(R.mipmap.map_mode);
    }
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    startguiji.setCompoundDrawables(drawable, null, null, null);
}

    //指南针 获取degree值
    private SensorEventListener listener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magenticValues = new float[3];

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //判断当前是加速度传感器还是地磁传感器
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = sensorEvent.values.clone();
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magenticValues = sensorEvent.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magenticValues);
            SensorManager.getOrientation(R, values);
            //Log.d("LocationDemo","value[0] is"+Math.toDegrees(values[0]));
            degree = Math.toDegrees(values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    //如果打开了地图页面就给服务器发送一个当前位置，每隔几秒会给服务器发自己的位置并且请求所有好友的位置。。关闭这个页面的时候给服务器发一个取消位置的信息然后服务器吧位置设为null
    public void addOthersLocation(double latitute, double longtitute, Bitmap touxiang) {

        Resources r = this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.icon_geo);//红点

//        //构建Marker图标
//        othersCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);

        //构建Marker图标
        othersCurrentMarker = BitmapDescriptorFactory
                .fromBitmap(mergeBitmap(bmp, touxiang));//public static BitmapDescriptor fromBitmap(Bitmap image)  + mergeBitmap()

        //定义Maker坐标点
        LatLng point = new LatLng(latitute, longtitute);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(othersCurrentMarker);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    //将两张图片合并为一张图片 用作头像
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        int Width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight();
        secondBitmap = zoomImage(secondBitmap, Width, height);
        Bitmap bitmap = Bitmap.createBitmap(Width, height * 2,
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(secondBitmap, new Matrix(), null);
        canvas.drawBitmap(firstBitmap, 0, height, null);
        return bitmap;
    }

    //缩放头像图片
    public Bitmap zoomImage(Bitmap bgimage, double newWidth,
                            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            chronometer.setText(formatseconds());
            mHandler.postDelayed(this, 1000);
        }
    }

    private MyRunnable mRunnable = null;
    /**
     * 定位SDK监听函数
     */
    String time1="",time2="";
    public class MyLocationListenner implements BDLocationListener {
        int i = 0;

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
//            Log.d("longtitude",location.getLongitude()+"");
//            Log.d("Latitude",location.getLatitude()+"");
            i++;
            if (i % 10 == 0) {
                //发送自己的位置，退出页面的时候给服务器发个null
                new Thread() {
                    @Override
                    public void run() {
                        //NetWorkClass.postJson(NetWorkClass.BuildLongLatiJson(location.getLongitude(),location.getLatitude()),"/updateLocation");
//                        try {
//                            Log.d("longtitude", location.getLongitude() + "");
//                            Log.d("Latitude", location.getLatitude() + "");
//
//                            NetWorkClass.postlocation(location.getLongitude() + "", location.getLatitude() + "");
//                        } catch (IOException e) {
//                            Log.e("locationdemo", "ioexception");
//                        }

                    }
                }.start();

                //同时请求好友位置(更新频率差不多就一起请求就行
                new Thread() {
                    @Override
                    public void run() {
                        //获取到好友位置不为"null"就显示

                    }
                }.start();

            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction((float) degree).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //定义Maker坐标点
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                 MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // 设置缩放比例,更新地图状态
                float f = mBaiduMap.getMaxZoomLevel();// 19.0
                 u = MapStatusUpdateFactory.newLatLngZoom(ll,
                        f-2);
                mBaiduMap.animateMapStatus(u);
                //地图位置显示
              //  Toast.makeText(DynamicDemo.this, location.getAddrStr(),
                      //  Toast.LENGTH_SHORT).show();

            }

            if (guiji) {
                if (isFirstGuiji) {
                    //第一个点很重要，决定了轨迹的效果，gps刚开始返回的一些点精度不高，尽量选一个精度相对较高的起始点
                    LatLng ll = null;

                    ll = getMostAccuracyLocation(location);
                    if (ll == null) {
                        return;
                    }
                    System.out.println(String.valueOf(location.getLatitude()));
                    //Toast.makeText(DynamicDemo.this,String.valueOf(String.valueOf(location.getLatitude())),Toast.LENGTH_LONG).show();
                    dingweiqueding=dingweiqueding+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude())+",";
                    isFirstGuiji = false;
                    points.add(ll);//加入集合
                    last = ll;

//                    //显示当前定位点，缩放地图
                    locateAndZoom(location, ll);

                    //标记起点图层位置
                    MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                    oStart.icon(startBD);// 设置覆盖物图片
                    mBaiduMap.addOverlay(oStart); // 在地图上添加此图层

                }
                //从第二个点开始
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                tvMileage=(TextView)findViewById(R.id.tvMileage);
               tvSpeed=(TextView)findViewById(R.id.tvSpeed);
               tvcalor=(TextView)findViewById(R.id.tvcalor);
//                DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                PathRecord record = null;
//                record.addpoint(ll);
//
//                //计算配速
//                distance = getDistance(points);
//                Toast.makeText(DynamicDemo.this,"julishi "+distance,Toast.LENGTH_SHORT).show();
//                double sportMile = distance / 1000d;

//                if (seconds > 0 && sportMile > 0.01) {
//                    double distribution = (double) seconds / 60d / sportMile;
//                    record.setDistribution(distribution);
//                    tvSpeed.setText(decimalFormat.format(distribution));
//                    tvMileage.setText(decimalFormat.format(sportMile));
//                } else {
//                    record.setDistribution(0d);
//                    tvSpeed.setText(String.valueOf("0.00"));
//                    tvMileage.setText(String.valueOf("0.00"));
//                }

                //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为5米才添加到集合中
                if (DistanceUtil.getDistance(last, ll) < 3) {//||DistanceUtil.getDistance(last, ll)>8
                    return;
                }
                dingweiqueding=dingweiqueding+String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude())+",";

                points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

                last = ll;

//                //显示当前定位点，缩放地图
                locateAndZoom(location, ll);

                //清除上一次轨迹，避免重叠绘画
                mMapView.getMap().clear();

                //起始点图层也会被清除，重新绘画
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(0));
                oStart.icon(startBD);
                mBaiduMap.addOverlay(oStart);

                //将points集合中的点绘制轨迹线条图层，显示在地图上
                OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
                mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                //}

//                 * 获取配速和运动距离*/
//                //距离的计算
//计算配速
                time2=getChronometerSeconds(chronometer);
               distance = getDistance(points);
               decimalFormat = new DecimalFormat("0.00");
               sportMile = distance / 1000d;
                seconds=Integer.parseInt(time2);
                distribution = (double) distance/seconds;
                 power=(sportMile/9)*555;
                tvSpeed.setText(decimalFormat.format(distribution));
                tvMileage.setText(decimalFormat.format(sportMile));
                tvcalor.setText(decimalFormat.format(power));
            }


        }

    }
    private void locateAndZoom(final BDLocation location, LatLng ll) {
        mCurrentLat = location.getLatitude();
        mCurrentLon = location.getLongitude();
        locData = new MyLocationData.Builder().accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        builder = new MapStatus.Builder();
        mCurrentZoom = 19.0f;
        builder.target(ll).zoom(mCurrentZoom);
      //  Toast.makeText(DynamicDemo.this, String.valueOf(mCurrentZoom), Toast.LENGTH_SHORT).show();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
    /**
     *
     * @param cmt  Chronometer控件
     * @return 小时+分钟+秒数  的所有秒数
     */
    public   String getChronometerSeconds(Chronometer cmt) {
        int totalss = 0;
        String string = cmt.getText().toString();
        int a=0;
                a=string.length();
        //Toast.makeText(DynamicDemo.this, string, Toast.LENGTH_SHORT).show();
        //Toast.makeText(DynamicDemo.this, "dage "+a, Toast.LENGTH_SHORT).show();
            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours =hour*3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins =min*60;
            int  SS =Integer.parseInt(split[2]);
            totalss = Hours+Mins+SS;
            return String.valueOf(totalss);
    }

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     * 你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     * 并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     * 这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     * gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(BDLocation location) {

        if (location.getRadius() > 40) {//gps位置精度大于40米的点直接弃用
            return null;
        }

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

        if (DistanceUtil.getDistance(last, ll) > 100) {
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 5) {
            points.clear();
            return ll;
        }
        return null;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;

        if (mSensorManager != null) {
            mSensorManager.unregisterListener(listener);
        }

        new Thread() {
            @Override
            public void run() {
//                //NetWorkClass.postJson(NetWorkClass.BuildLongLatiJson(location.getLongitude(),location.getLatitude()),"/updateLocation");
//                try {
//                    NetWorkClass.postlocation("null", "null");
//                } catch (IOException e) {
//                    Log.e("locationdemo", "ioexception");
//                }
            }
        }.start();

        super.onDestroy();
    }


    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }

        } else {
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }
    //计算距离
    private float getDistance(List<LatLng> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            LatLng firstLatLng = list.get(i);
            LatLng secondLatLng = list.get(i + 1);
            double betweenDis = DistanceUtil.getDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }
    public String formatseconds() {
        String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds
                / 3600;
        String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + ""
                : "0" + (seconds % 3600) / 60;
        String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + ""
                : "0" + (seconds % 3600) % 60;

        seconds++;

        return hh + ":" + mm + ":" + ss;
    }

}
