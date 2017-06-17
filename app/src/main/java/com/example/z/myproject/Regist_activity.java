package com.example.z.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
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
 * Created by z on 2017/4/13.
 */

public class Regist_activity extends Activity implements View.OnClickListener,TextWatcher
{
    TextView jump_login;

EditText phoneEdit;
    EditText nameEdit;
    EditText passwordEdit;
    EditText suerpassEdit;

    private SharedPreferences sp = null;// 声明一个SharedPreferences
    StringBuffer sb;
    HttpURLConnection conn;
    URL httpUrl;
    Toast toast;
    String phone="";
    String username="";
    String password="";
    String sure_pass="";

    //String url="http://10.9.173.36:8080/sevletDemo/servlet/RegSev";
    String url="http://192.168.56.1:8080/sevletDemo/servlet/RegSev";
    Thread thread;
    ProgressDialog progressDialog;
    Button regist;
    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    progressDialog.dismiss();
                    sp=getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PHONE", phone);
                    editor.putString("PASSWORD", password);
                    editor.putString("USERNAME",username);
                    editor.commit();
                    Intent intent = new Intent(Regist_activity.this,
                            Login_activity.class);
                    startActivity(intent);
                    Regist_activity.this.finish();
                    break;
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(Regist_activity.this,"该手机号已被注册",Toast.LENGTH_SHORT).show();

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activty);

        initview();
        initevent();
    }

    private void initview()
    {

        jump_login= (TextView) findViewById(R.id.jump_login);
        phoneEdit= (EditText) findViewById(R.id.reg_phone);
        nameEdit= (EditText) findViewById(R.id.reg_name);
        passwordEdit= (EditText) findViewById(R.id.reg_pass);
        suerpassEdit= (EditText) findViewById(R.id.reg_sure);
        regist= (Button) findViewById(R.id.regist);


    }
   private void  initevent()
    {

        regist.setOnClickListener(this);
        jump_login.setOnClickListener(this);
        phoneEdit.addTextChangedListener(this);
        nameEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(this);
        suerpassEdit.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.regist:

                phone = phoneEdit.getText().toString();
                username=nameEdit.getText().toString();
                password=passwordEdit.getText().toString();
                sure_pass=suerpassEdit.getText().toString();
                if(phone.length()==11)
                {

                    if(password.length()>=6)
                    {
                        if(password.equals(sure_pass))
                        {
                            doregist();
                        }
                        else {
                            toast.makeText(Regist_activity.this,"两次密码请一致",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        toast.makeText(Regist_activity.this,"密码最少6位",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    toast.makeText(Regist_activity.this,"请输入正确位数电话号码",Toast.LENGTH_SHORT).show();

                }



                break;
            case R.id.jump_login:
                Intent intent=new Intent(Regist_activity.this,Login_activity.class);
                startActivity(intent);
                Regist_activity.this.finish();
                break;



        }
    }


    public void doregist()
    {

        progressDialog = new ProgressDialog(Regist_activity.this);
        progressDialog.setMessage("正在注册中...");

        progressDialog.show();

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub


                 String res=   doPost();
                Log.e("lyd","post");

                if(res.equals("SUCCUSS")) {
                    handler.sendEmptyMessage(0);
                }
                else {
                    handler.sendEmptyMessage(1);
                }
                }

        });
        thread.start();
    }

    private String  doPost()
    {
        try {
            httpUrl=new URL(url);
            conn= (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            OutputStream os=conn.getOutputStream();
            String content="phone="+phone+"&password="+password+"&username="+username;
            os.write(content.getBytes());
            BufferedReader reader =new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb=new StringBuffer();
            String str;
            while((str=reader.readLine())!=null){
                sb.append(str);

            }

            Log.e("lyd","sb"+sb.toString());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        phone = phoneEdit.getText().toString();
        username=nameEdit.getText().toString();
        password=passwordEdit.getText().toString();
        sure_pass=suerpassEdit.getText().toString();
        if((!phone.equals(""))&&(!username.equals(""))&&(!password.equals(""))&&(!sure_pass.equals("")))
        {
            regist.setEnabled(true);
        }
        else
        {
            regist.setEnabled(false);
        }
    }
}
