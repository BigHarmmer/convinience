package com.example.z.myproject;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.speech.SpeechRecognizer;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by z on 2017/3/30.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(App.this, SpeechConstant.APPID+"=58dcab6b");

        SDKInitializer.initialize(getApplicationContext());
      ImageLoaderConfiguration  imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(imageLoaderConfiguration);


        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }
}
