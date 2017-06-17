package com.example.z.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by z on 2017/5/2.
 */

public class QiuZhu_Detail  extends Activity{
    String username;
    String address;
    String time;
    String title;
    String content;
    int isFinish;
    String img_url;
    TextView tx_user,tx_addr,tx_time,tx_title,tx_content,tx_isFin;
    ImageView img,back;
    private ImageLoader mImageLoader;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            //  .showImageOnLoading(R.drawable.ic_stub)            //加载图片时的图片
            // .showImageForEmptyUri(R.drawable.ic_empty)         //没有图片资源时的默认图片
            //.showImageOnFail(R.drawable.ic_error)              //加载失败时的图片
            .cacheInMemory(true)                               //启用内存缓存
            .cacheOnDisk(true)                                 //启用外存缓存
            .considerExifParams(true)                          //启用EXIF和JPEG图像格式

            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qz_detail);
        mImageLoader=ImageLoader.getInstance();


        Bundle bundle=this.getIntent().getExtras();

        username=bundle.getString("username");
        address=bundle.getString("address");
        time=bundle.getString("time");
        title=bundle.getString("title");
        content=bundle.getString("content");
        img_url=bundle.getString("img_url");
        isFinish=bundle.getInt("isFinish");
        initview();

    }

    private void initview()
    {

        back= (ImageView) findViewById(R.id.qz_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tx_user= (TextView) findViewById(R.id.qz_detail_name);
        tx_addr= (TextView) findViewById(R.id.qz_detail_address);
        tx_time= (TextView) findViewById(R.id.qz_detail_time);
        tx_title= (TextView) findViewById(R.id.qz_detail_title);
        tx_content= (TextView) findViewById(R.id.qz_detail_content);
        tx_isFin= (TextView) findViewById(R.id.qz_detail_return);
        img= (ImageView) findViewById(R.id.qz_detail_img1);

        tx_user.setText(username);
        tx_addr.setText(address);
        tx_time.setText(time);
        tx_title.setText(title);
        tx_content.setText(content);

        if(isFinish==0)
        {
            tx_isFin.setText("正在受理中，请耐心等待");
        }
        else if(isFinish==1) {
            tx_isFin.setText("你的求助已提交到相关部门，正在受理中");
        }
        else {
            tx_isFin.setText("你的求助已解决");
        }

        mImageLoader.displayImage(img_url,img,options);
    }
}
