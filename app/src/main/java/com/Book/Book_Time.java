package com.Book;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.Book_service;
import com.example.z.myproject.R;
import com.network.UploadUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by z on 2017/5/7.
 */

public class Book_Time extends BaseActivity implements View.OnClickListener{
    boolean date_choose=false,time_choose=false,person_choose=false,place_choose=false;
    private int choice_pos0=0;
    private int choice_pos1=0;
    private int choice_pos2=0;
    private Calendar cal;
    List<Book_person>mlist;
    Book_person choice_person;
    String[]names;
    String title;
    String murl;
    Thread thread;
    String result;
    Context context=this;
    AlertDialog.Builder builder;
    private int myear,mmonth,mday;
    RelativeLayout placepick, datepick, timepick, rest_num, add_person, create_person;
    Button sure, cancel;
    TextView datepick_tv, timepick_tv, rest_tv, add_person_tv,placepick_tv;
Handler handler=new Handler()
{
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what)
        {
            case 0:
                rest_tv.setText(result);
                break;
            case 1:
                Log.e("lyd",result);
                Toast.makeText(context,"预约成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_time);
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString("theme");
        setTitle(title);
        initview();

        getDate();


    }

    private void initview() {
        placepick= (RelativeLayout) findViewById(R.id.place_pick);
        datepick = (RelativeLayout) findViewById(R.id.date_pick);
        timepick = (RelativeLayout) findViewById(R.id.time_pick);
        rest_num = (RelativeLayout) findViewById(R.id.rest_num);
        add_person = (RelativeLayout) findViewById(R.id.add_person);
        create_person = (RelativeLayout) findViewById(R.id.create_person);

        sure= (Button) findViewById(R.id.bt_book);
        cancel= (Button) findViewById(R.id.bt_cancel);

        placepick.setOnClickListener(this);
        datepick.setOnClickListener(this);
        timepick.setOnClickListener(this);
        rest_num.setOnClickListener(this);
        add_person.setOnClickListener(this);
        create_person.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);

        placepick_tv= (TextView) findViewById(R.id.place_pick_tv);
        datepick_tv= (TextView) findViewById(R.id.date_pick_tv);
        timepick_tv= (TextView) findViewById(R.id.time_pick_tv);
        rest_tv= (TextView) findViewById(R.id.rest_num_tv);
        add_person_tv= (TextView) findViewById(R.id.add_person_tv);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.place_pick:
                 builder = new AlertDialog.Builder(Book_Time.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("请选择地点");
               final String place[]=new String[]{"下沙社区便民服务中心","上沙社区便民服务中心","东方社区便民服务中心","元成社区便民服务中心","闻潮社区便民服务中心"};

                builder.setSingleChoiceItems(place, choice_pos0, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        choice_pos0=which;

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                       placepick_tv.setText(place[choice_pos0]);
                        place_choose=true;
                        murl= UploadUtil.baseIp+"userbook_count"+"?type="+title+
                                "&date="+datepick_tv.getText().toString()+"&bookCenter="+placepick_tv.getText().toString()+
                                "&timeid="+choice_pos1+1;
                        if(date_choose&&time_choose)
                        {
                            thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    network(0);
                                }
                            });
                            thread.start();

                        }


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();

                break;

            case R.id.date_pick:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        myear=year;
                        mmonth=month;
                        mday=dayOfMonth;
                        datepick_tv.setText(year+"-"+(++month)+"-"+dayOfMonth);
                        date_choose=true;
                        murl= UploadUtil.baseIp+"userbook_count"+"?type="+title+
                                "&date="+datepick_tv.getText().toString()+"&bookCenter="+placepick_tv.getText().toString()+
                                "&timeid="+choice_pos1+1;
                        if(place_choose&&time_choose)
                        {
                            thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    network(0);
                                }
                            });
                            thread.start();

                        }
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(Book_Time.this , 0,listener,myear,mmonth,mday);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis()+24*60*60*1000*3);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.show();
                break;
            case R.id.time_pick:
                AlertDialog.Builder builder = new AlertDialog.Builder(Book_Time.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("请选择时间");
                final String[] time = {"9:00-10:00", "10:00-11:00", "14:00-15:00","15:00-16:00"};

                builder.setSingleChoiceItems(time, choice_pos1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        choice_pos1=which;

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(date_choose&&place_choose)
                        {
                            timepick_tv.setText(time[choice_pos1]);
                            time_choose=true;

                                murl= UploadUtil.baseIp+"userbook_count"+"?type="+title+
                                    "&date="+datepick_tv.getText().toString()+"&bookCenter="+placepick_tv.getText().toString()+
                            "&timeid="+choice_pos1+1;
                            Log.e("lyd","11111");
                            thread=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    network(0);
                                }
                            });
                            thread.start();

                        }
                        else if(place_choose&&(!date_choose))
                        {
                            Toast.makeText(context,"请先选择日期",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"请先选择地点",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();

                break;
            case R.id.rest_num:

                break;
            case R.id.add_person:

                builder = new AlertDialog.Builder(Book_Time.this);
                builder.setIcon(R.mipmap.logo);
                builder.setTitle("请选择办事人");

                Save save=new Save(context);
                mlist=save.getDataList("save");

                if(mlist.size()!=0) {
                    names=new String[mlist.size()];
                    for(int i=0;i<mlist.size();i++)
                    {

                        names[i]=mlist.get(i).getName();
                    }

                    builder.setSingleChoiceItems(names, choice_pos2, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            choice_pos2=which;

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            choice_person=mlist.get(choice_pos2);
                            add_person_tv.setText(names[choice_pos2]);
                            person_choose=true;

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });
                    builder.show();
                }
                else {
                    Toast.makeText(context,"当前无可选用户，请添加",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.create_person:
                Intent intent=new Intent(context,Edit_msg.class);
                startActivity(intent);

                break;
            case R.id.bt_book:
                if(date_choose&&time_choose&&place_choose&&person_choose)
                {
                    murl=UploadUtil.baseIp+"bm/userbook_save"+"?type="+title+
                            "&date="+datepick_tv.getText().toString()+"&bookCenter="+placepick_tv.getText().toString()+
                            "&timeid="+choice_pos1+1+"&uid="+1+"&idCard="+choice_person.getIdCard()+"&name="+choice_person.getName()
                    +"&phone="+choice_person.getPhone();
                    thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            network(1);
                        }
                    });
                    thread.start();

                }


                break;
            case R.id.bt_cancel:
                finish();
                break;

        }

    }
    private void getDate() {
        cal=Calendar.getInstance();
        myear=cal.get(Calendar.YEAR);       //获取年月日时分秒
        mmonth=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        mday=cal.get(Calendar.DAY_OF_MONTH);
    }

    private void network(int flag){
        HttpURLConnection conn= null;
        try {
            conn = (HttpURLConnection) new URL(murl).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");//设置请求方式为GET
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buffer = new BufferedReader(isr);
            String inputLine = "";
            result="";
            while((inputLine = buffer.readLine()) != null){
                result+=inputLine;

            }

            buffer.close();
            isr.close();
            conn.disconnect();

            handler.sendEmptyMessage(flag);

        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}