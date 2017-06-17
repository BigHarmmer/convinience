package com.z.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;


import com.google.firebase.storage.UploadTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by z on 2017/4/18.
 */

public class UpLoadAsycTask  extends AsyncTask<String,Void,String>{
Context context;
    String title;
    String content;
    String phone;
    String username;
    String add;
    int len;
    String filepath[];

    public UpLoadAsycTask(Activity context)
    {
        this.context=context;
    }





    public  void UpLoadFile(String []str)
    {

        username=str[0];
        title=str[1];
        content=str[2];
        phone=str[3];
        add=str[4];

//        try {
//
//            HttpClient client=new DefaultHttpClient();
//            HttpPost httpPost=new HttpPost("http://192.168.1.140/bm/upload");
//
//
//
//
//
//
//
//
//        }
    }


    @Override
    protected String doInBackground(String... params) {

        len=params.length;
        filepath=new String[len-5];
        for(int i=0;i<len-5;i++)
        {
            filepath[0]=params[i+5];
        }
        UpLoadFile(params);


        return null;
    }
}
