package com.Service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;

/**
 * Created by z on 2017/5/28.
 */

public class Service_score extends BaseActivity {
    RatingBar ratingBar1;
    RatingBar ratingBar2;
    TextView score_level;
    TextView score_name;
    Button score_submit;
    String name;
    float service_score;
    String score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_rating);
        Intent intent=getIntent();
        name=intent.getExtras().getString("name");
        service_score=intent.getExtras().getFloat("score");
        setTitle("客服评分");
        initview();
    }
    private void initview()
    {
        score_name= (TextView) findViewById(R.id.score_name);
        score_name.setText(name);
        ratingBar1= (RatingBar) findViewById(R.id.rating1);
        ratingBar1.setRating(service_score);
        ratingBar2= (RatingBar) findViewById(R.id.rating2);
        score_level= (TextView) findViewById(R.id.score_level);
        score_submit= (Button) findViewById(R.id.score_submit);
        score_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data=new Intent();
                setResult(100,data);
                finish();
            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating==0)
                {
                    score_submit.setEnabled(false);
                    score_level.setText("尚未评分");
                }
                else {
                    score_level.setText("当前评分:"+rating);
                    score_submit.setEnabled(true);
                }
            }
        });
    }
}
