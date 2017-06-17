package com.example.z.myproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by z on 2017/4/25.
 */

public class Search_Detail  extends Activity{

    TextView title;
    TextView time;
    TextView content;

    String ques;
    String answ;
    String date;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_detail);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        ques=bundle.getString("title");
         answ=bundle.getString("content");
         date=bundle.getString("date");
        initview();

        title.setText(ques);
        content.setText(answ);
        time.setText(date);

    }
    private void initview()
    {
        title= (TextView) findViewById(R.id.search_detail_title);
        time= (TextView) findViewById(R.id.search_detail_time);
        content= (TextView) findViewById(R.id.search_detail_content);

        back= (ImageView) findViewById(R.id.search_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
