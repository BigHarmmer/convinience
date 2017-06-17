package com.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.z.myproject.R;
import com.example.z.myproject.Submit_activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/4/14.
 */

public class Tab02_fragment extends Fragment implements View.OnClickListener{


    FragmentPagerAdapter mAdapter;
    View v;
ImageView jump_sub;
    private List<Fragment>fragments;
    private ViewPager sec_viewpager;
    private ImageView img_qz;
    private ImageView img_zd;
    Fragment Tab_02_01;
    Fragment Tab_02_02;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tab_02,container,false);

        initview();
        initevent();
        return v;

    }



    private void initview()
    {
        jump_sub= (ImageView) v.findViewById(R.id.jump_sub);
        img_qz= (ImageView) v.findViewById(R.id.bt_qz);
        img_zd= (ImageView) v.findViewById(R.id.bt_zd);
        sec_viewpager= (ViewPager) v.findViewById(R.id.sec_viewpager);
        Tab_02_01=new Tab_02_01();
        Tab_02_02=new Tab_02_02();


    }
    private void initevent()
    {
        img_qz.setOnClickListener(this);
        img_zd.setOnClickListener(this);
        fragments=new ArrayList<Fragment>();
        fragments.add(Tab_02_01);
        fragments.add(Tab_02_02);
        mAdapter=new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        sec_viewpager.setAdapter(mAdapter);
        sec_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int currentItem = sec_viewpager.getCurrentItem();
                Log.e("lyd","item:"+currentItem);
                switch (currentItem)
                {
                    case 0:
                        img_qz.setImageResource(R.mipmap.bt_qz_on);
                        img_zd.setImageResource(R.mipmap.bt_zd_off);
                        break;
                    case 1:
                        img_qz.setImageResource(R.mipmap.bt_qz_off);
                        img_zd.setImageResource(R.mipmap.bt_zd_on);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        jump_sub.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bt_qz:
                img_qz.setImageResource(R.mipmap.bt_qz_on);
                img_zd.setImageResource(R.mipmap.bt_zd_off);
                sec_viewpager.setCurrentItem(0);

                break;
            case R.id.bt_zd:
                img_qz.setImageResource(R.mipmap.bt_qz_off);
                img_zd.setImageResource(R.mipmap.bt_zd_on);
                sec_viewpager.setCurrentItem(1);
                break;
            case R.id.jump_sub:
                Intent intent=new Intent(getActivity(),Submit_activity.class);
                startActivity(intent);
                break;

        }
    }
}