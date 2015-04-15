package com.example.sofietroedsson.tax;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sofietroedsson on 15-04-12.
 */

    public class Snakemodel {

        /**
         * Creates a new SnakeModel with the stated dimensions
         * measured in snake segments.
         * @param boardWidth The board width
         * @param boardHeight The board height
         */
        public Snakemodel(int boardWidth, int boardHeight) {
            this.boardWidth = boardWidth;
            this.boardHeight = boardHeight;
        }

        public static final int POINTS_PER_APPLE = 1;
        public static final int POINTS_PER_MEET = 2;

        private int boardWidth, boardHeight;

        private State mState = State.READY;
        private Direction mDirection = Direction.NORTH;

        private int score = 0;
        private String highScore = "";

        private ArrayList<Point> mSnakeTrail = new ArrayList<Point>();
        private ArrayList<Point> mApples = new ArrayList<Point>();
    private ArrayList<Point> mMeet = new ArrayList<Point>();

        private static final Random random = new Random();

        /**
         * Initializes a new game. A new snake trail of five segments
         * and two new apples (randomized) are created.
         * Game state is set to READY.
         */
        public void initNewGame() {
            mSnakeTrail.clear();
            mApples.clear();
            mMeet.clear();

            int startX = boardWidth/2;
            int startY = boardHeight/2;
            mDirection = Direction.NORTH;
            mSnakeTrail.add(new Point(startX, startY));
            mSnakeTrail.add(new Point(startX, startY+1));
            mSnakeTrail.add(new Point(startX, startY+2));
            mSnakeTrail.add(new Point(startX, startY+3));
            mSnakeTrail.add(new Point(startX, startY+4));

            addApple();
            addApple();
            addMeet();
            addMeet();

            score = 0;

            mState = State.READY;
            if(highScore.equals("")){

            }
        }

        /**
         * Returns a <em>copy</em> of the list representing the
         * snake trail (positions head to tail).
         * @return
         */
        @SuppressWarnings("unchecked")
        public ArrayList<Point> getSnakeTrail() {
            return (ArrayList<Point>) mSnakeTrail.clone();
        }

        /**
         * Returns a <em>copy</em> of the list of apples.
         * @return
         */
        @SuppressWarnings("unchecked")
        public ArrayList<Point> getApples() {
            return (ArrayList<Point>) mApples.clone();
        }
        public ArrayList<Point> getMeet() {
        return (ArrayList<Point>) mMeet.clone();
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
         * @param direction The new direction
         */
        public void setDirection(Direction direction) {
            if (mState != State.RUNNING)
                return; // Do nothing

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
         * @param newState The new state.
         */
        public void setState(State newState) {
            if (newState == mState)
                return;

            if (mState == State.PAUSED && newState == State.RUNNING) {
                mState = State.RUNNING;
            }
            else if (mState == State.READY && newState == State.RUNNING) {
                mState = State.RUNNING;

            }
            else if (mState == State.RUNNING && newState == State.LOSE
                    || newState == State.PAUSED) {
                mState = newState;
            }
            else if (mState != State.RUNNING && newState == State.READY) {
                initNewGame();
                mState = State.READY;
            }
        }

        /**
         * Move this snake one step in the current direction.
         * If we find an apple, remove it and update the score.
         */
        public void move() {
            if (mState == State.RUNNING) {
                updateSnake();
                updateApples();
                updateMeet();
            }
        }

        private void updateSnake() {
            Point head = mSnakeTrail.get(0);
            Point newHead = null;

            switch (mDirection) {
                case NORTH:
                    newHead = new Point(head.x, head.y - 1);
                    break;
                case SOUTH:
                    newHead = new Point(head.x, head.y + 1);
                    break;
                case EAST:
                    newHead = new Point(head.x + 1, head.y);
                    break;
                case WEST:
                    newHead = new Point(head.x - 1, head.y);
                    break;
                default:
                    break;
            }

            // Ormen åker in i vägen
            if (snakeIsOutsideBoard(newHead)) {
                setState(State.LOSE);
                return;
            }

            // Ormen kolliderar med sig själv
            for (Point body : mSnakeTrail) {
                if (newHead.equals(body)) {
                    setState(State.LOSE);
                    return;
                }
            }

            // So far, so good. Add the new head and remove the tail
            mSnakeTrail.add(0, newHead);
            mSnakeTrail.remove(mSnakeTrail.size() - 1);
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
            } while (mSnakeTrail.contains(newApple));

            mApples.add(newApple);
        }

        private void updateApples() {
            Point head = mSnakeTrail.get(0);
            // If head overlaps an apple - remove and add points
            boolean wasRemoved = mApples.remove(head);
            if (wasRemoved) {
                score += POINTS_PER_APPLE;
                addApple();
            }
        }
    private void addMeet() {
        Point newMeet = null;
        do {
            newMeet = new Point(random.nextInt(boardWidth),
                    random.nextInt(boardHeight));
            // Make sure we do not select a point under the snake trail
        } while (mSnakeTrail.contains(newMeet));

        mMeet.add(newMeet);
    }

    private void updateMeet() {
        Point head = mSnakeTrail.get(0);
        // If head overlaps an apple - remove and add points
        boolean wasRemoved = mMeet.remove(head);
        if (wasRemoved) {
            score += POINTS_PER_MEET;
            addMeet();
        }
    }
    FileReader readfile = null;
    BufferedReader reader = null;
    public String getHighscore(){
        try{
            readfile = new FileReader("highscore.dat");
            reader = new BufferedReader(readfile);
            return reader.readLine();
        }
        catch(Exception e){
            return "0";
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    }


