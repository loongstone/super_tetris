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

    private static final String TAG = "Tetris";

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
        boolean fallSucceed = tryToFall();
        if (!fallSucceed) {
            boolean gameOver = putPartToWall();
            if (gameOver) {
                onGameOver();
            }
        }
        //设置块,并刷新显示
        mTetrisView.setPart(currentPart);
        mTetrisView.postInvalidate();
    }

    /**
     * 使方块进行下落
     *
     * @return true:可以继续下落  false:已经下落到底, 或者落在其他元素顶部,则无法下落
     */
    private boolean tryToFall() {
        int oldLine = currentPart.bottomIndex;
        //下落一层
        currentPart.bottomIndex = oldLine + 1;
        boolean blocked = isBeBlocked(mTetrisView.getBlockPoints(), currentPart);
        //下落被挡住,无法下落,已经到达底部
        if (blocked) {
            Log.d(TAG, "tryToFall:");
            currentPart.bottomIndex = oldLine;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 当方块无法下落,并且已经落在底部或者已有元素上的时候,把方块加入到 Wall中 ,并检测游戏是否因为Wall高度用尽而结束
     *
     * @return 游戏是否结束
     */
    private boolean putPartToWall() {
        int x;
        int y;
        boolean isGameOver = false;
        //方块必须在 画布 的范围内
        if (currentPart.bottomIndex >= mTetrisView.getY() - 1) {
            return false;
        }
        //遍历方块中的子块,并设置到Wall 中,同时根据方块是否超出来判断游戏是否结束
        for (int j = 0; j < currentPart.getHeight(); j++) {
            for (int i = 0; i < currentPart.getWidth(); i++) {
                int newPosition = currentPart.transformPosition(i, j);
                x = Part.getXFromMixturePosition(newPosition);
                y = Part.getYFromMixturePosition(newPosition);
                if (x < 0 || y < 0) {
                    isGameOver = true;
                    continue;
                }
                if (currentPart.part[i][j]) {
                    mTetrisView.getBlockPoints().setPoint(x, y, true);
                }
            }
        }

        Wall wall = mTetrisView.getBlockPoints();
        int headLine = wall.getHeight() - 1;

        //是否应该清除此行
        boolean lineShouldRemove;
        //遍历所有行
        for (int j = headLine; j >= 0; ) {
            lineShouldRemove = true;
            ////遍历行的元素,检测此行是否需要被消除
            for (int i = 0; i < wall.getWidth(); i++) {
                if (!wall.getPoint(i, j)) {
                    lineShouldRemove = false;
                    j--;
                    break;
                }
            }
            //清除此行,通过交换行的方式,整体下移,填充被清除的行
            if (lineShouldRemove) {
                wall.clearLine(j);
                for (int k = j; k >= 1; k--) {
                    wall.exchangeLine(k, k - 1);
                }
            }
        }
        //添加到Wall之后清除当前块
        currentPart = null;
        return isGameOver;
    }

    /**
     * 是否被其他块或者界限挡住
     *
     * @param wall 方块地图数据
     * @param part 下落的方块
     * @return 下落方块是否与已有地图上的方块干涉
     */
    private boolean isBeBlocked(Wall wall, Part part) {
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
                if (y >= wall.getHeight()) {
                    return true;
                }
                if (x >= wall.getWidth()) {
                    return true;
                }
                Log.d(TAG, "isBeBlocked: x" + x + "  Y" + y + "   ==" + wall.getHeight());
                if (wall.getPoint(x, y) && part.part[i][j]) {
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
        Wall wall = mTetrisView.getBlockPoints();
        if (mTetrisView != null && wall != null) {
            wall.clear();
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
            if (isBeBlocked(mTetrisView.getBlockPoints(), currentPart)) {
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
            if (isBeBlocked(mTetrisView.getBlockPoints(), currentPart)) {
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
