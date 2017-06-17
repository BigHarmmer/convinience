package com.example.z.myproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adapter.MyqzAdapter;
import com.bean.NewsBean;
import com.network.UploadUtil;
import com.widget.SwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/5/31.
 */

public class MyQiuzhu extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView lv;
    SwipeRefreshView sr;
    Thread thread;
    private int mCount;
    private MyqzAdapter mAdapter;
    List<NewsBean> mList;

    String murl= UploadUtil.baseIp+"userquestion_listAll";

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    mAdapter = new MyqzAdapter(mContext,mList);
                    lv.setAdapter(mAdapter);
                    break;
                case 1:
                    mAdapter = new MyqzAdapter(mContext,mList);
                    lv.setAdapter(mAdapter);
                    break;
            }

        }
    };


    /**
     * 正在加载状态
     */
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_qiuzhu);

        setTitle("我的求助");

        initview();
        initevent();
    }

    private void initview()
    {


        sr=(SwipeRefreshView) findViewById(R.id.my_swipe_qz);
        lv= (ListView) findViewById(R.id.my_lv_qz);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean bean=mList.get(position);

                Intent intent = new Intent(mContext, QiuZhu_Detail.class);
                Bundle bundle = new Bundle();
                Log.e("lyd","bean"+bean.getTime());
                bundle.putString("username", bean.getUsername());
                bundle.putString("address", bean.getAddress());
                bundle.putString("time", bean.getTime());
                bundle.putString("title", bean.getTitle());
                bundle.putString("content", bean.getContent());
                bundle.putInt("isFinish",bean.getIsFinish());
                bundle.putString("img_url",bean.getImg1());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }
    private void initevent()
    {
// 设置适配器数据


        sr.setOnRefreshListener(this);
        sr.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);

        // 设置下拉加载更多
        sr.setOnLoadListener(new SwipeRefreshView.OnLoadListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        sr.setLoading(false);
                    }
                }, 1000);
            }
        });

        thread=new Thread(new Runnable() {
            @Override
            public void run() {

                DecodeJson(murl);
                handler.sendEmptyMessage(0);

            }
        });
        thread.start();



    }

    public List<NewsBean> DecodeJson(String url) {

        mList = new ArrayList<>();
        NewsBean myNewsbean;
        String line="";
        String result="";
        try {
            Log.e("lyd","sss");
            URL myurl = new URL(url);
            InputStream is = myurl.openStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            Log.e("lyd","sss");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
            Log.e("lyd","reslut"+result);

            JSONArray array=new JSONArray(result);



            for(int i=0;i<array.length();i++) {


                JSONObject item=array.getJSONObject(i);

                myNewsbean=new NewsBean();
                myNewsbean.setTitle(item.getString("title"));
                myNewsbean.setTime(item.getString("time"));
                myNewsbean.setContent(item.getString("content"));
                myNewsbean.setUsername("lyd");
                myNewsbean.setAddress(item.getString("address"));
                myNewsbean.setIsFinish(item.getInt("isfinsh"));
                myNewsbean.setImg1(item.getString("imgurl"));
                mList.add(0,myNewsbean);

                Log.e("lyd",item.getString("content"));


            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;

    }

    @Override
    public void onRefresh() {
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                DecodeJson(murl);
                handler.sendEmptyMessage(1);
            }
        });
        thread.start();
        sr.setRefreshing(false);
    }
}
