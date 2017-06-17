package com.Book;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;
import com.network.UploadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/5/6.
 */

public class My_book extends BaseActivity {
    ListView lv;
    Book_msg msg;
    List<Book_msg>mlist;
    Book_msgAdapter madapter;
    Context context=this;
    Thread thread;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            madapter=new Book_msgAdapter(mlist,context);
            lv.setAdapter(madapter);
        }
    };
    String murl= UploadUtil.baseIp+"userbook_list_id?uid=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("预约查询");
        setContentView(R.layout.my_book_list);

        initview();
    }

    private void initview(){
        lv= (ListView) findViewById(R.id.my_book_list);
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                mlist=UploadUtil.DecodeJson(murl);
                handler.sendEmptyMessage(0);
            }
        });

        thread.start();




    }
}
