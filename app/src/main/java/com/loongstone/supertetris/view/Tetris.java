package com.loongstone.supertetris.view;

import android.util.Log;

import com.loongstone.supertetris.TetrisView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * @author loongstone
 * @date 2017/11/26
 */

public class Tetris implements TetrisInterface {
    private static final String TAG = "Tetris";
    private Timer timer;
    private int mDelay = 600;
    private TetrisView mTetrisView;
    private TetrisView.Part part;
    private static final boolean[][] PART_T = {
            {false, true, false},
            {true, true, true},
    };

    public Tetris(TetrisView view) {
        part = new TetrisView.Part();
        part.part = PART_T;
        part.leftIndex = 4;
        mTetrisView = view;
    }

    @Override
    public void startGame() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onTimer();
                }
            }, 0, mDelay);
        }
    }

    @Override
    public void pauseGame() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            return;
        }
        startGame();
    }

    @Override
    public void stopGame() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void setGameSpeed(int intervalSecondTime) {

    }

    @Override
    public void onRemoveLine(int lines) {

    }

    @Override
    public void onBlocksFalled() {

    }


    @Override
    public void onLineFull() {

    }

    private int line = 0;

    @Override
    public void onTimer() {
        if (line >= mTetrisView.getBlockPoints()[0].length) {
            line = 0;
        }
        mTetrisView.getBlockPoints()[0][line] = !mTetrisView.getBlockPoints()[2][line];
        part.bottomIndex = line;
        mTetrisView.setPart(part);
        mTetrisView.postInvalidate();
        Log.d(TAG, "onTimer: ");
        line++;
    }

    private boolean left = false;
    private boolean rigth = false;

    @Override
    public void turnLeft() {
        left = true;
        if (part != null) {
            part.leftIndex--;
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void turnRight() {
        rigth = true;
        if (part != null) {
            part.leftIndex++;
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

}
