package com.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.keeprunning1.R;
import com.google.android.material.navigation.NavigationView;
import com.home.fragment.ClockFragment;
import com.home.fragment.MapFragment;
import com.home.fragment.MusicFagment;
import com.user.User;
import com.user.UserOperator;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView imageView;
    private TextView textView;
    private UserOperator muserOperator;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        muserOperator = new UserOperator(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        LayoutInflater layoutInflater = this.getLayoutInflater();
//        view =layoutInflater.inflate(R.layout.home_header,null);
//        imageView=(ImageView)view.findViewById(R.id.userimageView);
//        textView=(TextView) view.findViewById(R.id.usernameView);
        Intent intent = getIntent();//获取Intent对象
        String username = intent.getStringExtra("username");
        textView=navigationView.getHeaderView(0).findViewById(R.id.usernameView);
        imageView=navigationView.getHeaderView(0).findViewById(R.id.userimageView);
        User bean = muserOperator.isExit(username);
        String userpicture = bean.userpicture;

        //Log.d("volley", userpicture);
        textView.setText(username);
            Bitmap bitmap= BitmapFactory.decodeFile(userpicture);

            imageView.setImageBitmap(bitmap);
        fm = this.getSupportFragmentManager();
        switchFragment("首页");
    }

    FragmentManager fm;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();
        switchFragment(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String[] titles = {"首页", "闹钟", "音乐", "系统设置"};
    Fragment[] fs = {new MapFragment(), new ClockFragment(), new MusicFagment()};
    //临时保存正被显示的Fragment
    Fragment mFragment = new MapFragment();

    public void switchFragment(String title) {
        FragmentTransaction t = fm.beginTransaction();
        Fragment f = fm.findFragmentByTag(title);
        //当前要求显示的Fragment，原来是否显示过，缓存过，如果加入过manager,就取出来显示就行了，没有就从数组中找
        if (f != null) {
            t.hide(mFragment).show(f);
            //保存正被显示的Fragment
            mFragment = f;
        } else {
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].equals(title)) {
                    t.hide(mFragment);
                    t.add(R.id.relativeLayout, fs[i], title);
                    //保存正被显示的Fragment
                    mFragment = fs[i];
                }
            }
        }
        t.commit();
        this.getSupportActionBar().setTitle(title);
    }
}