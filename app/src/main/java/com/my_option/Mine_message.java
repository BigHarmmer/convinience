package com.my_option;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.Book.Book_Cancel_Adapter;
import com.Service.ChatAi_act;
import com.Service.chat_history;
import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;

/**
 * Created by z on 2017/5/23.
 */

public class Mine_message extends BaseActivity{



    chat_history history;
   private LinearLayout clear_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_message);
        setTitle("我的消息");
        clear_chat= (LinearLayout) findViewById(R.id.clear_chat);
        clear_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("系统提示")//设置对话框标题
                        .setIcon(R.mipmap.logo)

                        .setMessage("确认清空聊天记录？")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                // TODO Auto-generated method stub

                                for(int i=0;i<7;i++)
                                {
                                    history=new chat_history(mContext,i);
                                    history.clearData();
                                }
                                history=new chat_history(mContext,100);
                                history.clearData();


                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();

            }
        });
    }

}
