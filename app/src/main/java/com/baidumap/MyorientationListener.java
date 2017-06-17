package com.baidumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by z on 2017/4/7.
 */

public class MyorientationListener implements SensorEventListener {


    private SensorManager mysenManager;
    private Sensor mySensor;
    private Context myContext;
    private float lastX;

    public MyorientationListener(Context context) {
        // TODO Auto-generated constructor stub

        this.myContext=context;
    }

    public void start() {
        // TODO Auto-generated method stub
        mysenManager =(SensorManager) myContext.getSystemService(Context.SENSOR_SERVICE);
        mySensor=mysenManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mysenManager.registerListener(this, mySensor, mysenManager.SENSOR_DELAY_UI);

    }
    public void stop() {
        // TODO Auto-generated method stub
        mysenManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if(mySensor.getType()==Sensor.TYPE_ORIENTATION)
        {
            float x=event.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX)>1.0)
            {
                lastX=x;
            }

            myOnOrientationListener.OnOrientationChanged(lastX);
        }



    }

    private OnOrientationListener myOnOrientationListener;

    public void setMyOnOrientationListener(
            OnOrientationListener myOnOrientationListener) {
        this.myOnOrientationListener = myOnOrientationListener;

    }

    public interface OnOrientationListener
    {
        void OnOrientationChanged(float x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


}
