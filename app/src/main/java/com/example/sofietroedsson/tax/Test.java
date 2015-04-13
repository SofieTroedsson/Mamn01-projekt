package com.example.sofietroedsson.tax;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by sofietroedsson on 15-04-13.
 */
public class Test extends ActionBarActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensorAccelerometer,sensorManager.SENSOR_DELAY_NORMAL);

    }
    private TextView text;
    private long lastupdate=0;
    private float last_x,last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType()== Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if((curTime-lastupdate)> 100){
                long difftime = (curTime-lastupdate);
                lastupdate =curTime;

                float speed = Math.abs(x+ y+ z - last_x - last_y - last_z)/difftime * 10000;

                if(speed>SHAKE_THRESHOLD){

                }
                last_x =x;
                last_y=y;
                last_z=z;
            }
           TextView text = (TextView)findViewById(R.id.x);
            text.setText("X:"+last_x);

            text = (TextView)findViewById(R.id.y);
            text.setText("Y:"+last_y);

            text = (TextView)findViewById(R.id.z);
            text.setText("Z:"+last_z);

        }

    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorAccelerometer,sensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
