package com.example.sofietroedsson.tax;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by sofietroedsson on 15-04-12.
 */
public class Taxview extends View {

        private Taxmodel taxModel;
        private Drawable greenImage, yellowImage, backImage;
        private Drawable[] taxImage, bodyImage;
        private long mLastMove, mMoveDelay = 400; // milliseconds
        private Context context;
        private Matrix matrix;
        private AccelerometerListener accelerometerListener;
    private boolean mListening;



    public Taxview(Context context, AttributeSet attributes) {
            super(context, attributes);

            // Load images
            Resources res = context.getResources();
            taxImage = new Drawable[4];
            bodyImage = new Drawable[2];
            greenImage = res.getDrawable(R.drawable.greenstar);
            bodyImage[0] = res.getDrawable(R.drawable.body);
            bodyImage[1] = res.getDrawable(R.drawable.body_flat);
            yellowImage = res.getDrawable(R.drawable.yellowstar);
            taxImage[0] = res.getDrawable(R.drawable.taxhuvud_north);
            taxImage[1] = res.getDrawable(R.drawable.taxhuvud_east);
            taxImage[2] = res.getDrawable(R.drawable.taxhuvud_south);
            taxImage[3] = res.getDrawable(R.drawable.taxhuvud_west);

        backImage = res.getDrawable( R.drawable.background);
            matrix = new Matrix();
            this.context = context;
            accelerometerListener = new AccelerometerListener();
        mListening = false;



    }

        public void startAnimation() {
            taxModel.initNewGame();
            taxModel.setState(State.RUNNING);
            this.update();
        }

        public void pauseAnimation() {
            taxModel.setState(State.PAUSED);

        }

        public void resumeAnimation() {
            if (taxModel != null && taxModel.getState() == State.PAUSED) {
                taxModel.setState(State.RUNNING);
                this.update();
            }
        }

        // This method defines what to draw in this view.
        // The method is called, implicitly, through view.invalidate().
        public void onDraw(Canvas canvas) {
            if(taxModel == null) {
                initModel();
                startAnimation();
            }

                //drawBorder(canvas);
                drawApples(canvas);
                drawTrail(canvas);
                drawStatus(canvas);
                drawMeet(canvas);


        }
        public Taxmodel getModel() { return taxModel; }

        private void turnSnakeOnTouchEvent(MotionEvent event) {
            // Touch screen pressed
            int centerX = (int) widthPixels / 2;
            int centerY = (int) heightPixels / 2;
            int x = (int) event.getX();
            int y = (int) event.getY();

            if (taxModel.getDirection() == Direction.SOUTH ||
                    taxModel.getDirection() == Direction.NORTH) {
                if (x < centerX) {
                    taxModel.setDirection(Direction.WEST);
                } else {
                    taxModel.setDirection(Direction.EAST);
                }
            } else { // i.e. WEST or EAST
                if (y < centerY) {
                    taxModel.setDirection(Direction.NORTH);
                } else {
                    taxModel.setDirection(Direction.SOUTH);
                }
            }
        }
        private void drawBorder(Canvas canvas) {
            mPaint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, (int) widthPixels, (int) heightPixels, mPaint);
            int border = 2;
            mPaint.setColor(Color.WHITE);
            canvas.drawRect(border, border, (int) (widthPixels - border),
                    (int) (heightPixels - border), mPaint);
        }

        private void drawTrail(Canvas canvas) {
            for (Point p : taxModel.getTaxTrail()) {
                int left = (int) (tileXPixels * p.x);
                int top = (int) (tileYPixels * p.y);
                int right = (int) (tileXPixels * (p.x + 1));
                int bottom = (int) (tileYPixels * (p.y + 1));
                if(p.getType() == Point.HEAD){
                    if(p.getDirection().equals(Direction.NORTH)) {
                        taxImage[0].setBounds(left, top, right, bottom);
                        taxImage[0].draw(canvas);
                    }
                    else if (p.getDirection().equals(Direction.EAST)){
                        taxImage[1].setBounds(left, top, right, bottom);
                        taxImage[1].draw(canvas);
                    }
                    else if (p.getDirection().equals(Direction.SOUTH)){
                        taxImage[2].setBounds(left, top, right, bottom);
                        taxImage[2].draw(canvas);
                    }
                    else if (p.getDirection().equals(Direction.WEST)){
                        taxImage[3].setBounds(left, top, right, bottom);
                        taxImage[3].draw(canvas);
                    }
                }
                else{
                    if(p.getDirection().equals(Direction.NORTH) || p.getDirection().equals(Direction.SOUTH)) {
                        bodyImage[0].setBounds(left, top, right, bottom);
                        bodyImage[0].draw(canvas);
                    }
                    else if(p.getDirection().equals(Direction.EAST) || p.getDirection().equals(Direction.WEST)){
                        bodyImage[1].setBounds(left, top, right, bottom);
                        bodyImage[1].draw(canvas);
                    }
                }
            }
        }


        private void drawApples(Canvas canvas) {
            for (Point p : taxModel.getApples()) {
                int left = (int) (tileXPixels * p.x);
                int top = (int) (tileYPixels * p.y);
                int right = (int) (tileXPixels * (p.x + 1));
                int bottom = (int) (tileYPixels * (p.y + 1));
                yellowImage.setBounds(left, top, right, bottom);
                yellowImage.draw(canvas);
            }
        }

    private void drawMeet(Canvas canvas) {
        for (Point p : taxModel.getMeat()) {
            int left = (int) (tileXPixels * p.x);
            int top = (int) (tileYPixels * p.y);
            int right = (int) (tileXPixels * (p.x + 1));
            int bottom = (int) (tileYPixels * (p.y + 1));
            greenImage.setBounds(left, top, right, bottom);
            greenImage.draw(canvas);
        }
    }

        private void drawStatus(Canvas canvas) {
            mPaint.setTextSize(36);
            mPaint.setColor(Color.BLACK);
            String message = "Score: " + taxModel.getScore() + " | "
                    + taxModel.getState().toString();
            canvas.drawText(message, 100, (int) (heightPixels - 100), mPaint);
        }

        private float widthPixels, heightPixels, tileXPixels, tileYPixels;
        private final Paint mPaint = new Paint();

        private void initModel() {
            int tilesX = 20;
            int tilesY = this.getHeight() * tilesX / this.getWidth();
            taxModel = new Taxmodel(tilesX, tilesY, context);
            calculateMetrics();


            Log.i("initModel","taxModel created! Everything setup. Game started");
        }

        private void calculateMetrics() {
            widthPixels = this.getWidth();
            heightPixels = this.getHeight();
            tileXPixels = widthPixels / taxModel.getBoardWidth();
            tileYPixels = heightPixels / taxModel.getBoardHeight();
        }

        // This is a simple form of animation, executed on the main thread
        public void update() {
            if (taxModel.getState() == State.RUNNING) {
                long now = System.currentTimeMillis();

                if (now - mLastMove > mMoveDelay) {
                    if(taxModel.increaseSpeed >=3){
                        mMoveDelay -= 100;
                        taxModel.increaseSpeed = 0;
                    }
                    taxModel.move();
                    this.invalidate();
                    mLastMove = now;
                }
                // Send a request for a new update after mMoveDelay ms
                mRedrawHandler.sleep(mMoveDelay);
            }
        }

        private RedrawHandler mRedrawHandler = new RedrawHandler();

        private class RedrawHandler extends Handler {

            @Override
            public void handleMessage(Message msg) {
                Taxview.this.update();
                Taxview.this.invalidate();
            }

            public void sleep(long delayMillis) {
                this.removeMessages(0);
                sendMessageDelayed(obtainMessage(0), delayMillis);
            }
        }

    // A listener for sensor events
    private class AccelerometerListener implements SensorEventListener {

        private final float ACC_THRESHOLD = 2.0F;

        @Override
        public void onSensorChanged(SensorEvent event) {
            double ax = event.values[0];
            double ay = event.values[1];
            double az = event.values[2];
            long time = event.timestamp;


            if(taxModel != null) {
                if (ay > ACC_THRESHOLD) {
                    taxModel.setLatestSensorDirection(Direction.SOUTH);
                } else if (ay < -ACC_THRESHOLD) {
                    taxModel.setLatestSensorDirection(Direction.NORTH);
                }
                if (ax > ACC_THRESHOLD) {
                    taxModel.setLatestSensorDirection(Direction.WEST);
                } else if (ax < -ACC_THRESHOLD) {
                    taxModel.setLatestSensorDirection(Direction.EAST);
                }
            }

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
    public void startListening(SensorManager manager) {
        if(!mListening){
            Sensor accelerometer = manager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(accelerometerListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
            mListening = true;
        }
    }

    public void stopListening(SensorManager manager) {
        if(mListening){
            manager.unregisterListener(accelerometerListener);
            mListening = false;
        }
    }

}

