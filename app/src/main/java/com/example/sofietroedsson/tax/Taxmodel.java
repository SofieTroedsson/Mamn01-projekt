package com.example.sofietroedsson.tax;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Vibrator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by sofietroedsson on 15-04-12.
 */

public class Taxmodel extends ActionBarActivity {
    private Tax tax;
    private MainActivity mainActivity;
    private boolean mListening;
    private Direction mLatestSensorDirection;
    public int increaseSpeed;

    //@param boardWidth The board width
    //@param boardHeight The board height

    public Taxmodel(int boardWidth, int boardHeight, Context context, ImageView imageText) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        mImageText = imageText;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.sound);
        mListening = false;
        increaseSpeed = 0;
    }


    public static final int POINTS_PER_APPLE = 1;
    public static final int POINTS_PER_MEAT = 2;

    private int boardWidth;
    private int boardHeight;
    private ImageView mImageText;

    private State mState = State.PAUSED;
    private Direction mDirection = Direction.NORTH;

    private int score = 0;
    private String highScore = "";
    private Matrix matrix = new Matrix();


    private ArrayList<Point> mTaxTrail = new ArrayList<Point>();
    private ArrayList<Point> mApples = new ArrayList<Point>();
    private ArrayList<Point> mMeat = new ArrayList<Point>();
    private Vibrator mVibrator;
    private MediaPlayer mPlayer;
    private static final Random random = new Random();

    /**
     * Initializes a new game. A new snake trail of five segments
     * and two new apples (randomized) are created.
     * Game state is set to READY.
     */
    public void initNewGame() {
        mTaxTrail.clear();
        mApples.clear();
        mMeat.clear();

        int startX = boardWidth / 2;
        int startY = boardHeight / 2;
        mDirection = Direction.NORTH;
        mTaxTrail.add(new Point(startX, startY, Point.HEAD));
        mTaxTrail.add(new Point(startX, startY + 1, Point.TAIL));
        mTaxTrail.add(new Point(startX, startY + 2, Point.TAIL));
        mTaxTrail.add(new Point(startX, startY + 3, Point.TAIL));
        mTaxTrail.add(new Point(startX, startY + 4, Point.TAIL));

        addApple();
        addApple();
        addMeat();
        addMeat();

        score = 0;

        mState = State.READY;
        if (highScore.equals("")) {

        }
    }


    public ArrayList<Point> getTaxTrail() {
        return (ArrayList<Point>) mTaxTrail.clone();
    }

    public ArrayList<Point> getApples() {
        return (ArrayList<Point>) mApples.clone();
    }

    public ArrayList<Point> getMeat() {
        return (ArrayList<Point>) mMeat.clone();
    }

    public Direction getDirection() {
        return mDirection;
    }

    public State getState() {
        return mState;
    }

    public long getScore() {
        return score;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Set the new direction for the snake. A direction anti parallel
     * with the current direction is ignored, to prevent the snake from
     * turning back on itself.
     *
     * @param direction The new direction
     */
    public void setDirection(Direction direction) {
        if (mState != State.RUNNING) {
            return; // Do nothing
        }
        // Prevent the snake from turning back on itself
        if (direction == Direction.NORTH) {
            if (mDirection != Direction.SOUTH) {
                mDirection = direction;
            }
            return;
        }

        if (direction == Direction.SOUTH) {
            if (mDirection != Direction.NORTH) {
                mDirection = direction;
            }
            return;
        }

        if (direction == Direction.EAST) {
            if (mDirection != Direction.WEST) {
                mDirection = direction;
            }
            return;
        }

        if (direction == Direction.WEST) {
            if (mDirection != Direction.EAST) {
                mDirection = direction;
            }
            return;
        }
    }

    /**
     * Set the new state for this snake game: READY, RUNNING, PAUSED or LOSE
     *
     * @param newState The new state.
     */
    public void setState(State newState) {
        if (newState == mState)
            return;

        if (mState == State.PAUSED && newState == State.RUNNING) {
            mState = State.RUNNING;
        } else if (mState == State.READY && newState == State.RUNNING) {
            mState = State.RUNNING;

        } else if (mState == State.RUNNING && newState == State.LOSE) {
            mState = newState;
            mImageText.setVisibility(View.VISIBLE);

        } else if (mState == State.RUNNING && newState == State.PAUSED) {
            mState = newState;

        } else if (mState != State.RUNNING && newState == State.READY) {
            Log.d("State", "Restarting now!");
            mImageText.setVisibility(View.INVISIBLE);
            initNewGame();
            mState = State.RUNNING;
        }
    }

    /**
     * Move this snake one step in the current direction.
     * If we find an apple, remove it and update the score.
     */
    public void move() {
        if (mState == State.RUNNING) {
            //updateSnake();
            setDirection(mLatestSensorDirection);
            updateApples();
            updateMeat();
            updateTaxhuvud();

        }
    }

    private void updateTaxhuvud() {
        Point oldHead = mTaxTrail.get(0);
        Point newHead = null;

        switch (mDirection) {
            case NORTH:
                newHead = new Point(oldHead.x, oldHead.y - 1, Point.HEAD);
                break;
            case SOUTH:
                newHead = new Point(oldHead.x, oldHead.y + 1, Point.HEAD);
                break;
            case EAST:
                newHead = new Point(oldHead.x + 1, oldHead.y, Point.HEAD);
                break;
            case WEST:
                newHead = new Point(oldHead.x - 1, oldHead.y, Point.HEAD);
                break;
            default:
                break;
        }
        newHead.setDirection(mDirection);

        if (snakeIsOutsideBoard(newHead)) {
            setState(State.LOSE);

            return;
        }

        for (Point body : mTaxTrail) {
            if (newHead.equals(body)) {
                setState(State.LOSE);
                return;

            }
        }
        //Now the old head is a tail!
        oldHead.setType(Point.TAIL);
        oldHead.setDirection(mDirection);

        //Add the new head and reduce the snakesize
        mTaxTrail.add(0, newHead);
        mTaxTrail.remove(mTaxTrail.size() - 1);
    }


    private boolean snakeIsOutsideBoard(Point head) {
        if (head.x >= boardWidth)
            return true;
        if (head.x < 0)
            return true;
        if (head.y >= boardHeight)
            return true;
        if (head.y < 0)
            return true;

        return false;
    }

    private void addApple() {
        Point newApple = null;
        do {
            newApple = new Point(random.nextInt(boardWidth),
                    random.nextInt(boardHeight));
            // Make sure we do not select a point under the snake trail
        } while (mTaxTrail.contains(newApple));

        mApples.add(newApple);
    }

    private void updateApples() {
        Point head = mTaxTrail.get(0);
        // If head overlaps an apple - remove and add points
        boolean wasRemoved = mApples.remove(head);
        if (wasRemoved) {
            mPlayer.start();
            mVibrator.vibrate(300);
            increaseSpeed += 1;
            score += POINTS_PER_APPLE;
            addApple();
            grow();
            // Kolla om score är större än 20 då ökar hastigheten
        }
    }

    private void addMeat() {
        Point newMeat = null;
        do {
            newMeat = new Point(random.nextInt(boardWidth),
                    random.nextInt(boardHeight));
            // Make sure we do not select a point under the snake trail
        } while (mTaxTrail.contains(newMeat));

        mMeat.add(newMeat);
    }

    private void updateMeat() {

        Point head = mTaxTrail.get(0);
        // If head overlaps an apple - remove and add points
        boolean wasRemoved = mMeat.remove(head);
        if (wasRemoved) {
            mPlayer.start();
            mVibrator.vibrate(300);
            score += POINTS_PER_MEAT;
            increaseSpeed += 1;
            grow();
            addMeat();

        }
    }


    private void grow() {
        Point addnew = mTaxTrail.get(1);
        mTaxTrail.add(1, addnew);
    }

    public void setLatestSensorDirection(Direction direction){
        mLatestSensorDirection = direction;
    }


    FileReader readfile = null;
    BufferedReader reader = null;

    public String getHighscore() {
        try {
            readfile = new FileReader("highscore.dat");
            reader = new BufferedReader(readfile);
            return reader.readLine();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}