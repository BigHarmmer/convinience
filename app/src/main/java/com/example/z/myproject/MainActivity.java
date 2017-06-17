package com.example.z.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fragment.Tab01_fragment;
import com.fragment.Tab02_fragment;
import com.fragment.Tab03_fragment;
import com.fragment.Tab04_fragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private boolean isFirstIn=true;
    private  Intent intent;
    //imageview
    private ImageView img_01;
    private ImageView img_02;
    private ImageView img_03;
    private ImageView img_04;

    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_04;
    Button button;
    MyLocationListener myLocationListener;
    LocationClient mLocationClient;
    String nowCity;
        SharedPreferences sp=null;
    SharedPreferences.Editor editor;
    //LinerLayout
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    //viewpager
    private ViewPager mViewPager;

    FragmentPagerAdapter mAdapter;

    Fragment tab_01;
    Fragment tab_02;
    Fragment tab_03;
    Fragment tab_04;


    private List<Fragment> mFragments;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        sp = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        editor=sp.edit();
        editor.putString("USERNAME","lyd");
        editor.putString("PHONE","17826856507");
        initView();
        initEvents();
        if(isFirstIn) {

            initLoc();
            isFirstIn=false;

            mLocationClient.start();

        }

    }

    private void initLoc()
    {

        mLocationClient=new LocationClient(this);

        myLocationListener=new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");

        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
    }



private void initEvents() {


    linearLayout1.setOnClickListener(this);
    linearLayout2.setOnClickListener(this);
    linearLayout3.setOnClickListener(this);
    linearLayout4.setOnClickListener(this);

}


    private void initView()
    {


        mViewPager= (ViewPager) findViewById(R.id.main_viewpager);


        tv_01= (TextView) findViewById(R.id.tv_tab1);
        tv_02= (TextView) findViewById(R.id.tv_tab2);
        tv_03= (TextView) findViewById(R.id.tv_tab3);
        tv_04= (TextView) findViewById(R.id.tv_tab4);
        img_01= (ImageView) findViewById(R.id.img_01);
        img_02= (ImageView) findViewById(R.id.img_02);
        img_03= (ImageView) findViewById(R.id.img_03);
        img_04= (ImageView) findViewById(R.id.img_04);

        linearLayout1= (LinearLayout) findViewById(R.id.id_tab_news);
        linearLayout2= (LinearLayout) findViewById(R.id.id_tab_contacts);
        linearLayout3= (LinearLayout) findViewById(R.id.id_tab_map);
        linearLayout4= (LinearLayout) findViewById(R.id.id_tab_me);

        mFragments=new ArrayList<Fragment>();

        tab_01=new Tab01_fragment();
        tab_02=new Tab02_fragment();
        tab_03=new Tab03_fragment();
        tab_04=new Tab04_fragment();



        mFragments.add(tab_01);
        mFragments.add(tab_02);
        mFragments.add(tab_03);
        mFragments.add(tab_04);
//检测网络状态
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
//            Toast.makeText(this,"网络不可用", Toast.LENGTH_SHORT).show();
//            return;
//        }


        mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };


        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                int currentItem = mViewPager.getCurrentItem();
                setTab(currentItem);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                // TODO Auto-generated method stub

            }
        });
    }

    private void setTab(int currentItem) {

        mViewPager.setCurrentItem(currentItem);
        resetImg1();
        switch (currentItem)
        {
            case 0:
                img_01.setImageResource(R.drawable.home_pressed);
                tv_01.setTextColor(Color.parseColor("#4faaf0"));
                break;
            case 1:
                img_02.setImageResource(R.drawable.help_pressed);
                tv_02.setTextColor(Color.parseColor("#4faaf0"));
                break;
            case 2:
                img_03.setImageResource(R.drawable.conv_pressed);
                tv_03.setTextColor(Color.parseColor("#4faaf0"));
                break;
            case 3:
                img_04.setImageResource(R.drawable.person_pressed);
                tv_04.setTextColor(Color.parseColor("#4faaf0"));
                break;
        }


    }


    private void resetImg1()
    {
        img_01.setImageResource(R.drawable.home_normal);
        img_02.setImageResource(R.drawable.help_normal);
        img_03.setImageResource(R.drawable.conv_normal);
        img_04.setImageResource(R.drawable.person_normal);

        tv_01.setTextColor(Color.parseColor("#d4d4d4"));
        tv_02.setTextColor(Color.parseColor("#d4d4d4"));
        tv_03.setTextColor(Color.parseColor("#d4d4d4"));
        tv_04.setTextColor(Color.parseColor("#d4d4d4"));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_news:
                resetImg1();
                img_01.setImageResource(R.drawable.home_pressed);
                setTab(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.id_tab_contacts:
                resetImg1();
                img_02.setImageResource(R.drawable.help_pressed);
                setTab(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.id_tab_map:
                resetImg1();
                img_03.setImageResource(R.drawable.conv_pressed);
                setTab(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.id_tab_me:
                resetImg1();
                img_04.setImageResource(R.drawable.person_pressed);
                setTab(3);
                mViewPager.setCurrentItem(3);
                break;


        }
  }
//   private PersonInfoListener myPersonInfoListener;
//
//    public void setMyPersonInfoListener(PersonInfoListener myPersonInfoListener) {
//        this.myPersonInfoListener = myPersonInfoListener;
//    }
//
//    public  interface PersonInfoListener
//    {
//        void getPerson(Person person);
//    }
public  class MyLocationListener implements BDLocationListener{

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        nowCity = bdLocation.getCity();
        SharedPreferences.Editor editor = sp.edit();
        Log.e("lyd",nowCity+"");
        editor.putString("LOCATION",nowCity);
        editor.apply();
    }
    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}

    @Override
    protected void onStop() {

        super.onStop();
        mLocationClient.stop();
    }
}

