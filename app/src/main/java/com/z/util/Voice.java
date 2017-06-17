package com.z.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.z.myproject.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;

import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by z on 2017/3/30.
 */




public class Voice {

    private PopupWindow mPopupWindow;
    private ImageView img_voice;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    String point_target;

    EditText mEdit;
    private static String TAG ;


    // 语音听写对象
    private com.iflytek.cloud.SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 语音听写UI

    Context mContext;
    private Toast mToast;

    int ret = 0; // 函数调用返回值
    String final_result;
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }

        }
    };

 public Voice(Context context,EditText edit,String target){

        mContext=context;

     point_target=target;
     mEdit=edit;
        TAG=mContext.getClass().getSimpleName();

        // 初始化识别无UI识别对象
     mIat= com.iflytek.cloud.SpeechRecognizer.createRecognizer(mContext,mInitListener);



     mEngineType = SpeechConstant.TYPE_CLOUD;
     initview();


    }
    void initview()
    {
        View popupview = LayoutInflater.from(mContext).inflate(R.layout.popvoice_layout,null);
        img_voice= (ImageView) popupview.findViewById(R.id.img_voice);
        mPopupWindow=new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }
    private void showTip( String str) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();

    }
    public void setParam() {
        // 清空参数


        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");


        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ISE_CATEGORY,"read_syllable");
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");


        //设置是否有符号
        mIat.setParameter(SpeechConstant.ASR_PTT,point_target);
        //设置前后端点超时
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");


    }

   public void doVoice()
   {


       FlowerCollector.onEvent(mContext, "iat_recognize");
       mIatResults.clear();
       setParam();


       ret = mIat.startListening(mRecognizerListener);
       Log.e("lyd","11111");
       if (ret != ErrorCode.SUCCESS) {
           showTip("听写失败,错误码：" + ret);
       } else {
           showTip("请说话");
       }


   }


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

       final_result=resultBuffer.toString();
        mEdit.setText(final_result);
        mEdit.setSelection(mEdit.getText().length());

    }


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
            //mPopupWindow.showAsDropDown(mButton);
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            mPopupWindow.dismiss();

            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);

            if (volume>5)
            {
                img_voice.setImageResource(R.drawable.voice_full);
            }
            else {
                img_voice.setImageResource(R.drawable.voice_empty);
            }
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            		Log.d(TAG, "session id =" + sid);
            	}

        }
    };


}
