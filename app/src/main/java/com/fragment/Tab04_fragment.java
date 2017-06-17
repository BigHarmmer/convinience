package com.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.z.myproject.Login_activity;
import com.example.z.myproject.R;
import com.my_option.Mine_message;


/**
 * Created by z on 2017/4/14.
 */

public class Tab04_fragment extends Fragment implements View.OnClickListener{




    LinearLayout logout;
    LinearLayout mine_message;
    String phonenum;
    TextView showname;
    SharedPreferences sp=null;
    SharedPreferences.Editor editor;

    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.tab_04,container,false);


        sp = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        editor=sp.edit();

        phonenum=sp.getString("PHONE","");








        initview();

        initevent();

        return v;

    }
    private void initview(){
        logout= (LinearLayout) v.findViewById(R.id.logout);

        mine_message= (LinearLayout) v.findViewById(R.id.mine_message);
        RelativeLayout mUserProfile = (RelativeLayout) v.findViewById(R.id.start_user_profile);
        LinearLayout mMineSetting = (LinearLayout) v.findViewById(R.id.mine_setting);


        LinearLayout mMineAbout = (LinearLayout) v.findViewById(R.id.mine_about);
        showname= (TextView) v.findViewById(R.id.show_name);

        showname.setText(phonenum);

        mUserProfile.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);

        logout.setOnClickListener(this);
        mine_message.setOnClickListener(this);
        mMineAbout.setOnClickListener(this);
    }
    private void initevent()
    {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(getActivity(), Login_activity.class));
                break;
            case R.id.mine_message:
                startActivity(new Intent(getActivity(), Mine_message.class));
//                case R.id.start_user_profile:
//                    startActivity(new Intent(getActivity(), MyAccountActivity.class));
//                    break;
//                case R.id.mine_setting:
//                    startActivity(new Intent(getActivity(), AccountSettingActivity.class));
//                    break;



        }

    }



}