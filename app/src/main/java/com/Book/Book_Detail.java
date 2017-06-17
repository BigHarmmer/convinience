package com.Book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;
import com.iflytek.cloud.thirdparty.S;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/5/7.
 */

public class Book_Detail extends BaseActivity{

    ListView listView;

    String str[]=new String[]{"门牌证办理","门牌号申领","社团管理","低收入家庭认证","困难家庭社会救助"};


    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_detail_list);
        Bundle bundle=this.getIntent().getExtras();
        String title=bundle.getString("theme");
        setTitle(title);

        initview();
    }

    private void initview()
    {
        listView= (ListView) findViewById(R.id.book_detail_list);

        adapter=new ArrayAdapter<String>(this,R.layout.book_detail_item,R.id.book_detail_txt,str);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Book_Detail.this,Book_Time.class);
                Bundle bundle=new Bundle();
                bundle.putString("theme", str[position]);
                intent.putExtras(bundle);
                Book_Detail.this.startActivity(intent);
            }
        });



    }

}
