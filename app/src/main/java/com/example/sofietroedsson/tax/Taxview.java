package com.example.sofietroedsson.tax;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
public class Taxview extends View{

        private Taxmodel taxModel;
        private Drawable greenImage, redImage, yellowImage, taxImage;
        private long mLastMove, mMoveDelay = 400; // milliseconds

        public Taxview(Context context, AttributeSet attributes) {
            super(context, attributes);

            // Load images
            Resources res = context.getResources();
            greenImage = res.getDrawable(R.drawable.greenstar);
            redImage = res.getDrawable(R.drawable.redstar);
            yellowImage = res.getDrawable(R.drawable.yellowstar);
            taxImage = res.getDrawable(R.drawable.taxhuvud);

            this.setOnTouchListener(new SnakeTouchListener());
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

            if (taxModel == null) {
                initModel();
                startAnimation();
            }

            drawBorder(canvas);
            drawApples(canvas);
            drawTrail(canvas);
            drawTaxhuvud(canvas);
            drawStatus(canvas);
            drawMeet(canvas);
        }

        // This class defines actions on touch events.
        private class SnakeTouchListener implements View.OnTouchListener {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        turnSnakeOnTouchEvent(event);
                        return true;
                    case (MotionEvent.ACTION_MOVE):
                        // ...
                        return true;
                    case (MotionEvent.ACTION_UP):
                        // ...
                        return true;
                    case (MotionEvent.ACTION_CANCEL):
                        // ...
                        return true;
                    case (MotionEvent.ACTION_OUTSIDE):
                        // ...
                        return true;
                    default:
                        return true; // The event is consumed
                }
            }
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

        public Taxmodel getTaxModel() {
            return taxModel;
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
                redImage.setBounds(left,top, right, bottom);
                redImage.draw(canvas);
            }
        }

        private void drawTaxhuvud(Canvas canvas){
            for(Point p : taxModel.getTaxhuvud()){
                int left = (int) (tileXPixels * p.x);
                int top = (int) (tileYPixels * p.y);
                int right = (int) (tileXPixels * (p.x + 1));
                int bottom = (int) (tileYPixels * (p.y + 1));
                taxImage.setBounds(left,top, right, bottom);
                taxImage.draw(canvas);
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
            mPaint.setColor(Color.DKGRAY);
            String message = "Score: " + taxModel.getScore() + " | "
                    + taxModel.getState().toString();
            canvas.drawText(message, 100, (int) (heightPixels - 100), mPaint);
        }

        private float widthPixels, heightPixels, tileXPixels, tileYPixels;
        private final Paint mPaint = new Paint();

        private void initModel() {
            int tilesX = 20;
            int tilesY = this.getHeight() * tilesX / this.getWidth();
            taxModel = new Taxmodel(tilesX, tilesY);
            calculateMetrics();

            taxModel.initNewGame();
        }

        private void calculateMetrics() {
            widthPixels = this.getWidth();
            heightPixels = this.getHeight();
            tileXPixels = widthPixels / taxModel.getBoardWidth();
            tileYPixels = heightPixels / taxModel.getBoardHeight();
        }

        // This is a simple form of animation, executed on the main thread
        public void update() {
            Log.i("SnakeView", "updated");
            if (taxModel.getState() == State.RUNNING) {
                long now = System.currentTimeMillis();

                if (now - mLastMove > mMoveDelay) {
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
    }

