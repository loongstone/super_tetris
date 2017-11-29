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
    private Timer timer;
    private int mDelay = 500;
    private TetrisView mTetrisView;
    private int line = -1;
    private int shap = 0;
    private Part part;

    public Tetris(TetrisView view) {
        part = new Part();
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

    @Override
    public void onTimer() {
        line++;
        if (line >= mTetrisView.getBlockPoints()[0].length || line < 0) {
            line = 0;
            shap++;
            shap %= Part.test.length;
            part.part = Part.test[shap];
            part.bottomIndex = 0;
            part.leftIndex = 5;
        }
        boolean falled = fall(part.bottomIndex + 1);
        if (falled) {
            line = 100;
        }
        mTetrisView.setPart(part);
        mTetrisView.postInvalidate();
        Log.d(Part.TAG, "onTimer: ");
    }

    private boolean fall(int line) {
        int oldLine = part.bottomIndex;
        part.bottomIndex = line;
        if (isBlockOverride(mTetrisView.getBlockPoints(), part)) {
            part.bottomIndex = oldLine;
            Log.d(Part.TAG, "fall:");
            boolean gameEnd = savePart();
            if (gameEnd) {
                Log.d(Part.TAG, "fall: GameOver");
                pauseGame();
            }
            return true;
        }
        return false;
    }

    private boolean savePart() {
        int x;
        int y;
        boolean gameEnd = false;
        for (int j = 0; j < part.getHeight(); j++) {
            for (int i = 0; i < part.getWidth(); i++) {
                int newPosition = part.transformPosition(i, j);
                x = Part.getXFromMixturePosition(newPosition);
                y = Part.getYFromMixturePosition(newPosition);
                if (x < 0 || y < 0) {
                    gameEnd = true;
                    continue;
                }
                if (part.part[i][j]) {
                    mTetrisView.getBlockPoints()[x][y] = true;
                }
            }
        }
        return gameEnd;
    }

    @Override
    public void turnLeft() {
        if (part != null) {
            part.leftIndex--;
            if (isBlockOverride(mTetrisView.getBlockPoints(), part)) {
                part.leftIndex++;
            }
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void turnRight() {
        if (part != null) {
            part.leftIndex++;
            if (isBlockOverride(mTetrisView.getBlockPoints(), part)) {
                part.leftIndex--;
            }
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void shift() {
        if (part != null) {
            int old = part.direction;
            part.direction++;
            part.direction %= 4;
            if (isBlockOverride(mTetrisView.getBlockPoints(), part)) {
                part.direction = old;
                return;
            }
            mTetrisView.setPart(part);
            mTetrisView.postInvalidate();
        }
    }

    private boolean isBlockOverride(final boolean[][] map, Part part) {
        int x;
        int y;
        for (int j = 0; j < part.getHeight(); j++) {
            for (int i = 0; i < part.getWidth(); i++) {
                int newPosition = part.transformPosition(i, j);
                x = Part.getXFromMixturePosition(newPosition);
                y = Part.getYFromMixturePosition(newPosition);
                if (x < 0 || y < 0) {
                    continue;
                }
                if (y >= map[0].length) {
                    return true;
                }
                if (x >= map.length) {
                    return true;
                }
                Log.d(Part.TAG, "isBlockOverride: x" + x + "  Y" + y + "   ==" + map[0].length);
                if (map[x][y] && part.part[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
