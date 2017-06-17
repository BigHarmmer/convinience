package com.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by z on 2017/5/23.
 */

public class Message_time {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String time;
    Long savetime;
    Long currenttime;


    public Message_time(Context context)
    {
        sp=context.getSharedPreferences("message_time",MODE_PRIVATE);
        editor=sp.edit();

    }

     public boolean check_time()
    {
        time=sp.getString("time","");
        if(time.equals(""))
        {
            time=System.currentTimeMillis()+"";
        }
        savetime=Long.parseLong(time);
        currenttime= System.currentTimeMillis();
        if(currenttime-savetime>=30*1000){
            return true;
        }
        else
        {
            return false;
        }

    }
    public void save_time()
    {
        currenttime=System.currentTimeMillis();
        editor.putString("time",currenttime+"");
        editor.commit();
    }
    public String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(currenttime);

        String result=formatter.format(date);

        Log.e("lyd",result);
        return result;
    }
}
