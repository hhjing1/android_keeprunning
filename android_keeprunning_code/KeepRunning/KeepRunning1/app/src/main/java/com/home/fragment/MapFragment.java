package com.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.calendar.CalendarFragment;
import com.showlocation.*;
import com.example.keeprunning1.R;
import com.google.android.material.tabs.TabLayout;

public class MapFragment extends Fragment {
    private TabLayout tvTab;
    private ViewPager tvPager;

    //定义标签数组
    private String[] titles = {"打卡", "运动记录", "今日情况"};

    public MapFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvTab = (TabLayout) view.findViewById(R.id.tv_tab);
        tvPager = (ViewPager) view.findViewById(R.id.tv_pager);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getFragmentManager(), 1);
        tvPager.setAdapter(myPagerAdapter);
        tvTab.setupWithViewPager(tvPager);


    }

    //建立ViewPager的适配器
    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new CalendarFragment();
            if (position == 0) {
                fragment = new CalendarFragment();
            } else if (position == 1) {
                fragment = new showlocationFragment();
            } else if (position == 2) {
                fragment = new MapChildThreeFragment();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
