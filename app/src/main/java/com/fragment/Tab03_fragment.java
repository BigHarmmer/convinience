package com.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.z.myproject.Book_service;
import com.example.z.myproject.Map_activity;
import com.example.z.myproject.MyQiuzhu;
import com.example.z.myproject.R;
import com.example.z.myproject.Servie_act;

/**
 * Created by z on 2017/4/14.
 */

public class Tab03_fragment extends Fragment implements View.OnClickListener {

LinearLayout service;
LinearLayout map;
    LinearLayout book;
    LinearLayout myhelp;
    Intent intent;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.tab_03,container,false);

        initview();


        initevent();

        return v;

    }
    private  void initview()
    {

       map= (LinearLayout) v.findViewById(R.id.tab3_map);
        service= (LinearLayout) v.findViewById(R.id.tab3_service);
myhelp= (LinearLayout) v.findViewById(R.id.tab3_myhelp);
        book= (LinearLayout) v.findViewById(R.id.tab3_book);

    }
    private  void initevent()
    {

        myhelp.setOnClickListener(this);
        map.setOnClickListener(this);
        service.setOnClickListener(this);
        book.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

          case R.id.tab3_map:
            intent=new Intent(getActivity(),Map_activity.class);
              startActivity(intent);
              break;
            case R.id.tab3_service:
                intent=new Intent(getActivity(),Servie_act.class);
                startActivity(intent);
                break;
            case R.id.tab3_book:
                intent=new Intent(getActivity(), Book_service.class);
                startActivity(intent);
                break;
            case R.id.tab3_myhelp:
                intent=new Intent(getActivity(), MyQiuzhu.class);
                startActivity(intent);
                break;

        }
    }
}