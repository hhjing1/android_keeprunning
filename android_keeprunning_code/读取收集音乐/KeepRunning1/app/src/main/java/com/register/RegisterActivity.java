package com.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.login.LoginActivity;
import com.example.keeprunning1.R;
import com.user.User;
import com.user.UserOperator;

import java.io.File;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //注册信息
    private ImageButton ibNavigationBack;
    private EditText etRegisterUsername;
    private EditText etRegisterPwdInput;
    private EditText etRegisterPwdsInput;
    private Button btRegisterSubmit;
    //头像
    private Button chooseFromAlbum;
    private ImageView userPicture;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String userpicture = "";
    //提交，验证
    private List<User> userList;
    private UserOperator muserOperator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        muserOperator = new UserOperator(this);
        init();
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }


    private void init() {
        ibNavigationBack = (ImageButton) findViewById(R.id.ib_navigation_back);
        userPicture = (ImageView) findViewById(R.id.user_picture);
        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        etRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        etRegisterPwdInput = (EditText) findViewById(R.id.et_register_pwd_input);
        etRegisterPwdsInput = (EditText) findViewById(R.id.et_register_pwds_input);
        btRegisterSubmit = (Button) findViewById(R.id.bt_register_submit);

        //按钮点击事件，返回登录界面，头像选择，确定注册
        ibNavigationBack.setOnClickListener(this);
        chooseFromAlbum.setOnClickListener(this);
        btRegisterSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.choose_from_album:
                chosePicture();
                break;
            case R.id.bt_register_submit:
                String username = etRegisterUsername.getText().toString().trim();
                String psw = etRegisterPwdInput.getText().toString().trim();
                String psw1 = etRegisterPwdsInput.getText().toString().trim();
                if (!psw.equals(psw1)) {
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
                //判断不为空且不是空串
                else if (TextUtils.isEmpty(username) || TextUtils.isEmpty(psw) || TextUtils.isEmpty(psw1)) {
                    Toast.makeText(this, "存请将信息填写完整", Toast.LENGTH_SHORT).show();
                } else {
                    User bean = muserOperator.isExit(username);
                    if (bean != null) {
                        Toast.makeText(this, "该用户已存在，请重新注册", Toast.LENGTH_SHORT).show();
                    } else {
                        User beans = new User(username, psw, userpicture, "0");
                        muserOperator.addUser(beans);
                        Intent intent2 = new Intent(this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("password", psw);
                        intent2.putExtras(bundle);
                        setResult(0x11, intent2);
                        finish();
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

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
            userPicture.setImageBitmap(photo);
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
