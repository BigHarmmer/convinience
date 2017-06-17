package com.Book;

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

public class Save {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public Save(Context context) {

        this.context=context;
        preferences =context.getSharedPreferences("person_list",
                MODE_PRIVATE);
        editor=preferences.edit();
    }

    public  void addDataList(String tag, Book_person person) {
        List<Book_person> datalist=getDataList(tag);

        datalist.add(person);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    public  void setDataList(String tag, List<Book_person>datalist) {

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }



    public  List<Book_person> getDataList(String tag) {
        List<Book_person> datalist=new ArrayList<Book_person>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<Book_person>>() {
        }.getType());
        return datalist;

    }


}
