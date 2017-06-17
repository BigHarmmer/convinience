package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.z.myproject.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.bean.NewsBean;

import java.util.List;

/**
 * Created by z on 2017/4/19.
 */

public class MyqzAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private List<NewsBean> mNewsbeen;
    private LayoutInflater mInflater;
    private String murl1;
    private String murl2;
    private String murl3;
    private int mCount;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            //  .showImageOnLoading(R.drawable.ic_stub)            //加载图片时的图片
            // .showImageForEmptyUri(R.drawable.ic_empty)         //没有图片资源时的默认图片
            //.showImageOnFail(R.drawable.ic_error)              //加载失败时的图片
            .cacheInMemory(true)                               //启用内存缓存
            .cacheOnDisk(true)                                 //启用外存缓存
            .considerExifParams(true)                          //启用EXIF和JPEG图像格式

            .build();
    public MyqzAdapter(Context context, List<NewsBean>newsbeen) {
        mInflater=LayoutInflater.from(context);
        mNewsbeen=newsbeen;
        mImageLoader=ImageLoader.getInstance();


    }

    @Override
    public int getCount() {
        return mNewsbeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsbeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh1;
        if(convertView==null)
        {
            convertView=mInflater.inflate(R.layout.qz_item,null);
            vh1=new ViewHolder();
            vh1.head= (ImageView) convertView.findViewById(R.id.qz_head_icon);
            vh1.username= (TextView) convertView.findViewById(R.id.qz_username);
            vh1.address= (TextView) convertView.findViewById(R.id.qz_add);
            vh1.title= (TextView) convertView.findViewById(R.id.qz_title);
            vh1.content= (TextView) convertView.findViewById(R.id.qz_content);
            vh1.img1= (ImageView) convertView.findViewById(R.id.qz_img1);
            vh1.img2= (ImageView) convertView.findViewById(R.id.qz_img2);
            vh1.img3= (ImageView) convertView.findViewById(R.id.qz_img3);
            convertView.setTag(vh1);
        }
        else
        {
            vh1= (ViewHolder) convertView.getTag();
        }
        murl1=mNewsbeen.get(position).getImg1();
        murl2=mNewsbeen.get(position).getImg2();
        murl3=mNewsbeen.get(position).getImg3();

        if(!"".equals(murl1))
        {
            vh1.img1.setTag(murl1);
            mImageLoader.displayImage(murl1,vh1.img1,options);
        }
        else {
            vh1.img1.setVisibility(View.GONE);
        }
        if(!"".equals(murl2))
        {
            vh1.img1.setTag(murl2);
            mImageLoader.displayImage(murl2,vh1.img2,options);
        }
        else {
            vh1.img2.setVisibility(View.GONE);
        }
        if(!"".equals(murl3))
        {
            vh1.img3.setTag(murl3);
            mImageLoader.displayImage(murl3,vh1.img3,options);
        }
        else {
            vh1.img3.setVisibility(View.GONE);
        }
        vh1.username.setText(mNewsbeen.get(position).getUsername());
        //vh1.username.setText(mNewsbeen.get(position).getUsername());
        vh1.address.setText(mNewsbeen.get(position).getAddress());
        vh1.title.setText(mNewsbeen.get(position).getTitle());
        vh1.content.setText(mNewsbeen.get(position).getContent());




        return convertView;
    }
    class ViewHolder{
        public ImageView head;
        public TextView username;
        public TextView address;
        public TextView title;
        public TextView content;
        public ImageView img1;
        public ImageView img2;
        public ImageView img3;


    }
}
