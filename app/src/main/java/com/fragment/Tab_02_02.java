package com.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.Itembean;
import com.adapter.MyNewAdapter;
import com.example.z.myproject.R;
import com.widget.SwipeRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/4/16.
 */

public class Tab_02_02 extends Fragment{


    private ListView lv;
    String URL="http://www.imooc.com/api/teacher?type=4&num=30";
    private List<Itembean> mNewsbean;
    List<Itembean> mNewsbean_list=new ArrayList<Itembean>();
    private  View mFooterView ;
    boolean isFirstin=true;
    boolean isLoadFinish=false;
    boolean flag=true;
    int json_num;
    int start=0;
    int end=10;
    MyNewAdapter myadapter;
    int load_num;
    SwipeRefreshView sr;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v=inflater.inflate(R.layout.layout_zd,container,false);

        initview();
        initevent();
        return v;
    }
    private void initview()
    {
        sr= (SwipeRefreshView) v.findViewById(R.id.sr_zhidao);
        lv= (ListView) v.findViewById(R.id.list_zd);
        mFooterView = View.inflate(getActivity(), R.layout.view_footer, null);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mNewsbean_list=new ArrayList<Itembean>();

                        myadapter.notifyDataSetChanged();

                        start=0;
                        end=10;
                        isFirstin=true;
                        isLoadFinish=false;

                        new Myasynctask().execute(URL);

                        sr.setRefreshing(false);
                    }
                },1000);
            }
        });
        sr.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);

        // 设置上拉拉加载更多
        sr.setOnLoadListener(new SwipeRefreshView.OnLoadListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isLoadFinish)
                    {
                        if(flag) {
                            TextView tx1 = new TextView(getActivity());
                            tx1.setText("已经加载到底部");
                            tx1.setGravity(Gravity.CENTER_HORIZONTAL);
                            lv.addFooterView(tx1);
                            flag = false;
                        }
                    }
                    else {
                            //加载新的列表
                        new Myasynctask().execute(URL);

                    }

                        // 加载完数据设置为不加载状态，将加载进度收起来
                        sr.setLoading(false);
                    }
                }, 1000);
            }
        });


    }
    private void initevent()
    {
        new Myasynctask().execute(URL);
    }
    class Myasynctask extends AsyncTask<String,Void,List<Itembean>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            lv.addFooterView(mFooterView);
            lv.setSelection(lv.getCount());

        }

        @Override
        protected List<Itembean> doInBackground(String... params) {
            return DecodeJson(params[0]);
        }

        @Override
        protected void onPostExecute(List<Itembean> newsbeen) {
            super.onPostExecute(newsbeen);
            lv.removeFooterView(mFooterView);
            if(isFirstin) {
                myadapter = new MyNewAdapter(getActivity(), newsbeen);
                lv.setAdapter(myadapter);
                isFirstin=false;
            }
            else {

                myadapter.notifyDataSetChanged();
            }

        }

        public List<Itembean> DecodeJson(String url)
        {

            Itembean myNewsbean;
            String line="";
            String result="";
            try {
                java.net.URL myurl=new URL(url);
                InputStream is=myurl.openStream();
                InputStreamReader isr=new InputStreamReader(is,"utf-8");
                BufferedReader br=new BufferedReader(isr);
                while ((line=br.readLine())!=null){
                    result+=line;
                }


                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                json_num=jsonArray.length();
                Log.e("lyd","jsonLenth"+json_num);


                if(end>=json_num)
                {
                    load_num=json_num-start;
                    end=json_num;

                    for(int i=start;i<load_num+start;i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        myNewsbean=new Itembean();
                        myNewsbean.setImageView(jsonObject.getString("picSmall"));
                        //Log.d("lyd",jsonObject.getString("picSmall"));
                        myNewsbean.setTitle(jsonObject.getString("name"));
                        //Log.d("lyd",jsonObject.getString("name"));
                        myNewsbean.setContent(jsonObject.getString("description"));
                        //Log.d("lyd",jsonObject.getString("description"));
                        mNewsbean_list.add(myNewsbean);
                    }
                    isLoadFinish=true;
                }else {

                    for(int i=start;i<end;i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        myNewsbean=new Itembean();
                        myNewsbean.setImageView(jsonObject.getString("picSmall"));
                        //Log.d("lyd",jsonObject.getString("picSmall"));
                        myNewsbean.setTitle(jsonObject.getString("name"));
                        //Log.d("lyd",jsonObject.getString("name"));
                        myNewsbean.setContent(jsonObject.getString("description"));
                        //Log.d("lyd",jsonObject.getString("description"));
                        mNewsbean_list.add(myNewsbean);
                    }
                   start=end;
                    end=end+10;

                }







            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return mNewsbean_list;
        }
    }

}
