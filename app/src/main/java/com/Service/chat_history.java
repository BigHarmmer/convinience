package com.Service;

/**
 * Created by z on 2017/5/23.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.Book.Book_person;
import com.bean.Msg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by z on 2017/5/8.
 */

public class chat_history {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public chat_history(Context context,int pos) {

        this.context=context;
        preferences =context.getSharedPreferences("chat_history"+pos,
                MODE_PRIVATE);
        editor=preferences.edit();
    }



    public  void addDataList(String tag, Msg msg) {
        List<Msg> datalist=getDataList(tag);

        datalist.add(msg);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    public  void setDataList(String tag, List<Msg>datalist) {

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }



    public  List<Msg> getDataList(String tag) {
        List<Msg> datalist=new ArrayList<Msg>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<Msg>>() {
        }.getType());
        return datalist;

    }
    public void clearData()
    {
        editor.clear();
        editor.commit();
    }


}
