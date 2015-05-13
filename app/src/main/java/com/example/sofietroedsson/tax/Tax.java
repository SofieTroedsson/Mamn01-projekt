package com.example.sofietroedsson.tax;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;

/**
 * Created by sofietroedsson on 15-04-01.
 */
public class Tax extends ActionBarActivity {

        private Taxview taxview;


    private SensorManager sensorManager;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.d("ONCREATE", "ONCREATE STARTING");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tax);

            taxview = (Taxview) findViewById(R.id.snake);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Log.d("ONCREATE", "ONCREATE DONE");

        }


        @Override
        protected void onPause() {
            super.onPause();
            taxview.pauseAnimation();
            taxview.stopListening(sensorManager);

            // Unregister the sensor listener


            Log.i("Snake", "MainActivity paused");
        }

        @Override
        protected void onResume() {
            super.onResume();
            taxview.startListening(sensorManager);


            // Register the sensor listener



            Log.i("Snake", "MainActivity resumed");
        }

    }


    //@Override
       // public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
         //   getMenuInflater().inflate(R.menu.menu_main, menu);
           // return true;
        //}

       // @Override
        //public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
          //  int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            //if (id == R.id.action_settings) {

              //  return true;
            //}

//            return super.onOptionsItemSelected(item);
        //}


