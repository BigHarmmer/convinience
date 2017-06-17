package com.example.z.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by z on 2017/4/12.
 */

public class Login_activity extends Activity implements View.OnClickListener{

    private Button login_button;
    private EditText phoneEdit;
    private EditText passwordEdit;

    TextView jump_regist;
    URL httpUrl;

    StringBuffer sb;
    HttpURLConnection conn;
    //String url="http://10.9.173.36:8080/sevletDemo/servlet/LogSev";
    String url="http://192.168.1.115:8080/sevletDemo/servlet/LogSev";
    boolean check=true;
    private String phone;
    private String password;
    private String username;

    private SharedPreferences sp = null;// 声明一个SharedPreferences
    private Thread thread;
    private ProgressDialog progressDialog;

    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PHONE", phone);
                    editor.putString("PASSWORD", password);
                    editor.putString("USERNAME",username);
                    editor.commit();
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login_activity.this,
                            MainActivity.class);
                    startActivity(intent);
                    Login_activity.this.finish();
                    break;
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(Login_activity.this,"帐号密码错误",Toast.LENGTH_SHORT).show();

                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        sp=getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        init();

        login();


    }
    public void init(){
        phoneEdit= (EditText) findViewById(R.id.phone);
        passwordEdit= (EditText) findViewById(R.id.password);
        login_button= (Button) findViewById(R.id.bt_login);
        jump_regist= (TextView) findViewById(R.id.jump_reg);
        jump_regist.setOnClickListener(this);
        login_button.setOnClickListener(this);


    }

    public void login()
    {

        if(!sp.getString("PHONE","").equals(""))
        {

            phone= sp.getString("PHONE","");
            password=sp.getString("PASSWORD","");

            username=sp.getString("USERNAME","");

            Log.e("lyd",phone+"---");
            dologin();

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                phone = phoneEdit.getText().toString();
                password = passwordEdit.getText().toString();

                dologin();
                break;
            case R.id.jump_reg:
                Intent intent =new Intent(Login_activity.this,Regist_activity.class);
                startActivity(intent);
                Login_activity.this.finish();
                break;



            default:
                break;
        }


    }


    public void dologin()
    {

        progressDialog = new ProgressDialog(Login_activity.this);
        progressDialog.setMessage("正在登录中...");

        progressDialog.show();

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(500);

                  String  res= doPost();
                    if(!res.equals("")) {

                        handler.sendEmptyMessage(0);
                    }
                    else{
                        handler.sendEmptyMessage(1);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //这里是连接数据库部分，为了方便起见，在下面采用固定帐号密码登录
//    private String doPost()
//    {
//        try {
//            httpUrl=new URL(url);
//            conn= (HttpURLConnection) httpUrl.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setReadTimeout(5000);
//            OutputStream os=conn.getOutputStream();
//            String content="phone="+phone+"&password="+password;
//            os.write(content.getBytes());
//            BufferedReader reader =new BufferedReader(new InputStreamReader(conn.getInputStream()));
//             sb=new StringBuffer();
//            String str;
//            while((str=reader.readLine())!=null){
//                sb.append(str);
//            }
//
//            Log.e("lyd","sb"+sb.toString());
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }

    private String doPost()
    {
        Log.e("lyd",phone +password);

        if(phone.equals("13763865313")&&password.equals("123456"))
        {

            return "lyd";
        }
        else {
            return "";
        }

    }

}
