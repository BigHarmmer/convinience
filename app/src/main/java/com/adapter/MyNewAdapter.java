package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.Itembean;
import com.example.z.myproject.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by z on 2017/4/19.
 */

public class MyNewAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private List<Itembean> mNewsbeen;
    private LayoutInflater mInflater;
    private String murl;
    private int mCount;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_stub)            //加载图片时的图片
            .showImageForEmptyUri(R.mipmap.ic_empty)         //没有图片资源时的默认图片
            .showImageOnFail(R.mipmap.ic_error)              //加载失败时的图片
            .cacheInMemory(true)                               //启用内存缓存
            .cacheOnDisk(true)                                 //启用外存缓存
            .considerExifParams(true)                          //启用EXIF和JPEG图像格式

            .build();

    public MyNewAdapter(Context context, List<Itembean>newsbeen) {
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
            convertView=mInflater.inflate(R.layout.zhidao_item,null);
            vh1=new ViewHolder();
            vh1.imageView= (ImageView) convertView.findViewById(R.id.zhidao_img);
            vh1.title= (TextView) convertView.findViewById(R.id.zhidao_title);
            vh1.content= (TextView) convertView.findViewById(R.id.zhidao_content);
            convertView.setTag(vh1);
        }
        else
        {
            vh1= (ViewHolder) convertView.getTag();
        }
        murl=mNewsbeen.get(position).getImageView();
        vh1.imageView.setImageResource(R.mipmap.ic_launcher);
        vh1.imageView.setTag(murl);

        mImageLoader.displayImage(murl,vh1.imageView,options);

        vh1.title.setText(mNewsbeen.get(position).getTitle());
        vh1.content.setText(mNewsbeen.get(position).getContent());



        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView title;
        public TextView content;

    }
}
