package com.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.keeprunning1.R;
import com.register.Utils;
import com.user.User;
import com.user.UserOperator;

import java.io.File;

public class PerMessageActivity extends Activity {

    private ImageButton ibNavigationBack;
    private ImageView userimage;
    private ImageView isok;
    private TextView user;
    private EditText username;
    private EditText qianming;
    private EditText useroldpsw;
    private EditText usernewpsw;
    private String usernames;
    private UserOperator muserOperator;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String userpicture = "";
    private String psw3;
    private String user1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.per_message);
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        ibNavigationBack = (ImageButton) findViewById(R.id.ib_navigation_back);
        userimage = (ImageView) findViewById(R.id.userimage);
        user = (TextView) findViewById(R.id.user);
        username = (EditText) findViewById(R.id.username);
        qianming = (EditText) findViewById(R.id.qianming);
        useroldpsw = (EditText) findViewById(R.id.useroldpsw);
        usernewpsw = (EditText) findViewById(R.id.usernewpsw);
        isok = (ImageView) findViewById(R.id.isok);
        muserOperator = new UserOperator(this);
        Intent intent = getIntent();//获取Intent对象
        usernames = intent.getStringExtra("username");
        Log.i("username", usernames);
        User bean = muserOperator.isExit(usernames);
        String userpicture = bean.userpicture;
        Bitmap bitmap = BitmapFactory.decodeFile(userpicture);
        userimage.setImageBitmap(bitmap);
        user1 = bean.username;
        user.setText(user1);
        psw3 = bean.userpsw;
        String nicheng1 = bean.nicheng;
        String qiangming1 = bean.qianming;
        username.setText(nicheng1);
        qianming.setText(qiangming1);
        ibNavigationBack.setOnClickListener(l);
        userimage.setOnClickListener(l);
        isok.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {    //通过获取点击的id判断点击了哪个张图片
                case R.id.ib_navigation_back:
                    finish();
                    break;
                case R.id.userimage:
                    chosePicture();
                    break;
                case R.id.isok:
                    String nicheng = username.getText().toString().trim();
                    String qianmings = qianming.getText().toString().trim();
                    String psw1 = useroldpsw.getText().toString().trim();
                    String psw2 = usernewpsw.getText().toString().trim();
                    if (!TextUtils.isEmpty(psw1) && !TextUtils.isEmpty(psw2)) {
                        if (!psw1.equals(psw3)) {
                            new AlertDialog.Builder(PerMessageActivity.this)
//                                    .setIcon(R.drawable.clock)
                                    .setTitle("错误")
                                    .setMessage("旧密码错误")
                                    .setPositiveButton("确定"
                                            , new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();
                        } else if (TextUtils.isEmpty(userpicture)){
                            User bean = new User();
                            bean.setUsername(user1);
                            bean.setNicheng(nicheng);
                            bean.setUserpsw(psw2);
                            bean.setQianming(qianmings);
                            muserOperator.updateuser(bean);
                            Toast.makeText(PerMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            refresh();
                        } else {
                            User bean = new User();
                            bean.setUsername(user1);
                            bean.setNicheng(nicheng);
                            bean.setUserpsw(psw2);
                            bean.setUserpicture(userpicture);
                            bean.setQianming(qianmings);
                            muserOperator.updateuser1(bean);
                            Toast.makeText(PerMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    } else if (TextUtils.isEmpty(userpicture)) {
                        User bean = new User();
                        bean.setUsername(user1);
                        bean.setNicheng(nicheng);
                        bean.setUserpsw(psw3);
                        bean.setQianming(qianmings);
                        muserOperator.updateuser(bean);
                        Toast.makeText(PerMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        refresh();
                    }else {
                        User bean = new User();
                        bean.setUsername(user1);
                        bean.setNicheng(nicheng);
                        bean.setUserpsw(psw3);
                        bean.setUserpicture(userpicture);
                        bean.setQianming(qianmings);
                        muserOperator.updateuser1(bean);
                        Toast.makeText(PerMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void refresh() {

        onCreate(null);

    }

    /*
   修改头像对话框
    */
    public void chosePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://从本地选择
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            userimage.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做圆形处理的，但已经被裁剪了

        String imagePath = Utils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        String m = "666";
        Log.d("volley", m);
        if (imagePath != null) {
            // 拿着imagePath上传了
            userpicture = imagePath;
        }
    }
}
