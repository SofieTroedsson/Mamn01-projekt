package com.example.sofietroedsson.tax;

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

/**
 * Created by sofietroedsson on 15-04-01.
 */
public class Tax extends ActionBarActivity{

        private Taxview snakeView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tax);

            snakeView = (Taxview) findViewById(R.id.snake);

            // Create the sensor listener
            accelerometerListener = new AccelerometerListener();
        }

        private SensorManager sensorManager;
        private Sensor accelerometerSensor;
        private AccelerometerListener accelerometerListener;

        // A listener for sensor events
        private class AccelerometerListener implements SensorEventListener {

            private final float ACC_THRESHOLD = 2.0F;

            @Override
            public void onSensorChanged(SensorEvent event) {
                double ax = event.values[0];
                double ay = event.values[1];
                double az = event.values[2];
                long time = event.timestamp;

                Snakemodel snakeModel = snakeView.getSnakeModel();

                if(ay > ACC_THRESHOLD) {
                    Log.i("SnakeSensor", "South down");
                    snakeModel.setDirection(Direction.SOUTH);
                }else if(ay < -ACC_THRESHOLD) {
                    Log.i("SnakeSensor", "North down");
                    snakeModel.setDirection(Direction.NORTH); }
                if(ax > ACC_THRESHOLD) {
                    Log.i("SnakeSensor", "West down");
                    snakeModel.setDirection(Direction.WEST);
                }else if(ax < -ACC_THRESHOLD) { Log.i("SnakeSensor", "East down");
                    snakeModel.setDirection(Direction.EAST); }

                // This is where you put the code checking the values
                // ax and ay (acceleration in x and y direction).
                // If ax > e.g. 2.0 m/s2, turn the snake WEST by calling
                // snakeModel.setDirection(Direction.WEST)
                // (or turn EAST? - you figure that out).
                // Then check the value of ay and so on.
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // …
            }
        }

        @Override
        protected void onPause() {
            super.onPause();
            snakeView.pauseAnimation();
            // Unregister the sensor listener
            stopListening();

            Log.i("Snake", "MainActivity paused");
        }

        @Override
        protected void onResume() {
            super.onResume();
            snakeView.resumeAnimation();
            // Register the sensor listener
            startListening();

            Log.i("Snake", "MainActivity resumed");
        }

        private void startListening() {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(accelerometerListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        private void stopListening() {
            if (sensorManager != null) {
                sensorManager.unregisterListener(accelerometerListener);
            }
        }




        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

