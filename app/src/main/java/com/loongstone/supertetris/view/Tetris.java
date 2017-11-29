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
    private int mDelay = 500;
    private TetrisView mTetrisView;
    private Part part;
    private static final boolean[][] PART_A = {
            {false, true, false},
            {true, true, true},
    };
    private static final boolean[][] PART_B = {
            {true, false, false},
            {true, true, true},
    };
    private static final boolean[][] PART_C = {
            {true, true, true, true},

    };
    private static final boolean[][] PART_D = {
            {true, true},
            {true, true},

    };
    private static boolean[][][] test = {PART_A, PART_B, PART_C, PART_D};

    public Tetris(TetrisView view) {
        part = new Part();
        part.part = PART_A;
        part.leftIndex = 4;
        part.direction = Part.DIR_90;
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

    private int line = -1;
    private int shap = 0;

    @Override
    public void onTimer() {
        line++;
        if (line >= mTetrisView.getBlockPoints()[0].length || line < 0) {
            line = 0;
            shap++;
            shap %= test.length;
        }
        //mTetrisView.getBlockPoints()[0][line] = !mTetrisView.getBlockPoints()[0][line];
        part.part = test[shap];
        part.bottomIndex = line;
        mTetrisView.setPart(part);
        mTetrisView.postInvalidate();
        Log.d(TAG, "onTimer: ");
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
        if (part != null) {
            part.leftIndex++;
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void shift() {
        if (part != null) {
            part.direction++;
            part.direction %= 4;
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    private boolean isBlockOverride() {
        return true;
    }
}
