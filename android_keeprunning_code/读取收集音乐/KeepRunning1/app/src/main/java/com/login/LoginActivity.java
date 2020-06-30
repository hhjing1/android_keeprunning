package com.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.keeprunning1.R;
import com.home.HomeActivity;
import com.register.RegisterActivity;
import com.user.User;
import com.user.UserOperator;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText etLoginUsername;
    private EditText etLoginPwd;
    private Button btLoginSubmit;
    private Button btLoginRegister;
    private CheckBox cbRememberLogin;
    private TextView tvLoginForgetPwd;
    private UserOperator muserOperator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        muserOperator = new UserOperator(this);
        initView();

    }

    public void initView() {
        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPwd = (EditText) findViewById(R.id.et_login_pwd);
        btLoginSubmit = (Button) findViewById(R.id.bt_login_submit);
        btLoginRegister = (Button) findViewById(R.id.bt_login_register);
        cbRememberLogin = (CheckBox) findViewById(R.id.cb_remember_login);
        tvLoginForgetPwd = (TextView) findViewById(R.id.tv_login_forget_pwd);

        btLoginSubmit.setOnClickListener(this);
        btLoginRegister.setOnClickListener(this);
        //判断是否记住过密码,如果上次登录记住过密码，则显示密码
        etLoginUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                } else {
                    String username = etLoginUsername.getText().toString().trim();
                    if (!TextUtils.isEmpty(username)) {
                        User bean = muserOperator.isExit(username);
                        if (bean!= null) {
                            String usercheck = bean.usercheck;
                            String userpsw = bean.userpsw;
                            Log.d("volley", usercheck);
                            if (usercheck.equals("1")) {
                                Log.d("volley", userpsw);
                                etLoginPwd.setText(userpsw);
                                cbRememberLogin.setChecked(true);
                            }
                        } else {
                            etLoginUsername.setText("");
                            etLoginUsername.setHint("账户不存在，请先注册");
                        }
                    } else {
                        etLoginUsername.setHint("请输入用户名");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 0x11);
                break;
            case R.id.bt_login_submit:
                String username = etLoginUsername.getText().toString().trim();
                String userpsw = etLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userpsw)) {
                    User bean = muserOperator.isExit(username);
//                    String userpsws = bean.userpsw;
//                    Log.d("volley", userpsws);
                    if (bean != null && bean.userpsw.equals(userpsw)) {
                        if (cbRememberLogin.isChecked()) {
                            String usercheck = "1";
                            User beans = new User(username, usercheck);
                            muserOperator.isRemPsw(beans);
                        }else{
                            String usercheck = "0";
                            User beans = new User(username, usercheck);
                            muserOperator.isRemPsw(beans);
                        }
                            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();

                            //向首页穿参数
                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();

                    } else {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x11 && resultCode == 0x11) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            etLoginPwd.setText(password);
            etLoginUsername.setText(username);
        }
    }
}
