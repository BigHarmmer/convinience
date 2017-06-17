package com.Book;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by z on 2017/5/7.
 */

public class Edit_msg extends BaseActivity {


    EditText person_name, person_phone, person_idcard;

    List<Book_person> persons=new ArrayList<Book_person>();
    Context context = this;
    String name, phone, idcard;
    Button person_sure;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_person_msg);

        initview();
    }

    private void initview() {
        person_name = (EditText) findViewById(R.id.person_name);
        person_phone = (EditText) findViewById(R.id.person_phone);
        person_idcard = (EditText) findViewById(R.id.person_idcard);
        person_sure = (Button) findViewById(R.id.person_sure);

        person_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = person_name.getText().toString();
                phone = person_phone.getText().toString();
                idcard = person_idcard.getText().toString();
                if (isMobileNO(phone)) {

                    Book_person person = new Book_person(name, phone, idcard);
                    Save mysave=new Save(context);
                    mysave.addDataList("save",person);
                    persons=mysave.getDataList("save");

                    Log.e("lyd","size"+persons.size());
                    finish();

                } else {
                    Toast.makeText(context, "手机号输入有误", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }






}
