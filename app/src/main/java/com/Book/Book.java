package com.Book;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/5/6.
 */

public class Book  extends BaseActivity{


    List<String> mData;
    RecyclerView mRecycleView;
    MyRecycleAdapter myAdapter;
    String []str=new String[]{"民政","人力社保","税务","住建","交警","城管","国土","商务","消防","市场监管"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        setTitle("我要预约");
        initData();
        initview();


    }
    private void initData()
    {
        mData=new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            mData.add(str[i]);

        }


    }

    private void initview() {

        mRecycleView = (RecyclerView) findViewById(R.id.recycle_view);

        myAdapter = new MyRecycleAdapter(mData);


        //设置布局管理器 , 将布局设置成纵向
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);

        //设置分隔线
        //mRecycleView.addItemDecoration(new DividerItemDecoration(this , DividerItemDecoration.VERTICAL_LIST));

        //设置增加或删除条目动画
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setAdapter(myAdapter);//设置适配器
        mRecycleView.setLayoutManager(gridLayoutManager);

    }
}
