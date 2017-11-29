package com.loongstone.supertetris.view;

import android.util.Log;

import com.loongstone.supertetris.TetrisView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author loongstone
 * @date 2017/11/26
 */

public class Tetris implements TetrisInterface {

    public static final String TAG = "Tetris";

    private Timer timer;
    private int mDelay = 500;
    private TetrisView mTetrisView;
    private int line = -1;
    private int shap = 0;
    /**
     * 当前显示的part
     */
    private Part currentPart;
    private int score = 0;
    private Queue<Part> partQueue = new LinkedList<>();


    public Tetris(TetrisView view) {
        mTetrisView = view;
    }

    @Override
    public void startNewGame() {
        clearMap();
        currentPart = null;
        //分数归零
        score = 0;
        startGame();
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
    public void onTimer() {
        //取得元素
        if (currentPart == null) {
            currentPart = getPartFromQueue();
        }
        boolean fallBottom = fall();
        mTetrisView.setPart(currentPart);
        mTetrisView.postInvalidate();
        if (fallBottom) {
            currentPart = getPartFromQueue();
        }
    }

    /**
     * 下落函数
     *
     * @return 是否下落到底, 或者落在其他元素顶部
     */
    private boolean fall() {
        int oldLine = currentPart.bottomIndex;
        currentPart.bottomIndex = oldLine + 1;

        if (isBlockOverride(mTetrisView.getBlockPoints(), currentPart)) {
            Log.d(TAG, "fall:");
            currentPart.bottomIndex = oldLine;
            boolean gameEnd = savePart();
            if (gameEnd) {
                Log.d(TAG, "GameOver");
                onGameOver();
            }
            return true;
        }
        return false;
    }

    /**
     * 保存,并且完成消除操作
     *
     * @return 游戏是否结束
     */
    private boolean savePart() {
        int x;
        int y;
        boolean gameEnd = false;
        if (currentPart.bottomIndex >= mTetrisView.getY() - 1) {
            return false;
        }
        for (int j = 0; j < currentPart.getHeight(); j++) {
            for (int i = 0; i < currentPart.getWidth(); i++) {
                int newPosition = currentPart.transformPosition(i, j);
                x = Part.getXFromMixturePosition(newPosition);
                y = Part.getYFromMixturePosition(newPosition);
                if (x < 0 || y < 0) {
                    gameEnd = true;
                    continue;
                }
                if (currentPart.part[i][j]) {
                    mTetrisView.getBlockPoints()[x][y] = true;
                }
            }
        }
        //TODO 消除
        return gameEnd;
    }

    /**
     * 是否被其他块或者界限挡住
     *
     * @param map  方块地图数据
     * @param part 下落的方块
     * @return 下落方块是否与已有地图上的方块干涉
     */
    private boolean isBlockOverride(boolean[][] map, Part part) {
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
                Log.d(TAG, "isBlockOverride: x" + x + "  Y" + y + "   ==" + map[0].length);
                if (map[x][y] && part.part[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 清空画布
     */
    private void clearMap() {
        if (mTetrisView != null && mTetrisView.getBlockPoints() != null) {
            boolean[][] map = mTetrisView.getBlockPoints();
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = false;
                }
            }
        }
    }


    /**
     * 从队列中获取块,保证队列始终至少有一个元素,作为预览;
     *
     * @return 返回队列元素
     */
    private Part getPartFromQueue() {
        if (partQueue.isEmpty()) {
            partQueue.add(Part.getRandomPart());
        }
        partQueue.add(Part.getRandomPart());
        return partQueue.remove();
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
    public void turnLeft() {
        if (currentPart != null) {
            currentPart.leftIndex--;
            if (isBlockOverride(mTetrisView.getBlockPoints(), currentPart)) {
                currentPart.leftIndex++;
            }
            mTetrisView.setPart(currentPart);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void turnRight() {
        if (currentPart != null) {
            currentPart.leftIndex++;
            if (isBlockOverride(mTetrisView.getBlockPoints(), currentPart)) {
                currentPart.leftIndex--;
            }
            mTetrisView.setPart(currentPart);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void shift() {
        if (currentPart != null) {
            int old = currentPart.direction;
            currentPart.direction++;
            currentPart.direction %= 4;
            //TODO 翻转阻碍情况的判断
            mTetrisView.setPart(currentPart);
            mTetrisView.postInvalidate();
        }
    }

    @Override
    public void onGameOver() {
        pauseGame();
    }
}
