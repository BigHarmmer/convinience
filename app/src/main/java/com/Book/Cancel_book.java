package com.Book;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class Cancel_book extends BaseActivity {
    ListView lv;

    List<Book_msg>mlist;
    Book_Cancel_Adapter madapter;
    Context context=this;
    Thread thread;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            madapter=new Book_Cancel_Adapter(mlist,context);
            madapter.setMyOnNotifyListener(new Book_Cancel_Adapter.OnNotifyListener() {
                @Override
                public void onNotifyChanged(final int pos) {
                    new AlertDialog.Builder(context)
                            .setTitle("系统提示")//设置对话框标题
                            .setIcon(R.mipmap.logo)

                            .setMessage("确认取消此预约？")//设置显示的内容

                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                    // TODO Auto-generated method stub

                                    mlist.remove(pos);

                                    madapter=new Book_Cancel_Adapter(mlist,mContext);
                                    lv.setAdapter(madapter);


                                }

                            }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮

                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件

                        }

                    }).show();
                }
            });
            lv.setAdapter(madapter);
        }
    };
    String murl= UploadUtil.baseIp+"userbook_list_id?uid=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("预约取消");
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
