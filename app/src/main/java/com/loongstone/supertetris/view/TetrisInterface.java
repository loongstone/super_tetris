package com.loongstone.supertetris.view;

/**
 * @author loongstone
 * @date 2017/11/26
 */

public interface TetrisInterface {
    /**
     * 开始游戏
     */
    void startGame();

    /**
     * 暂停由此
     */
    void pauseGame();

    /**
     * 结束游戏
     */
    void stopGame();

    /**
     * 设置游戏速度
     *
     * @param intervalMillSecondTime 下降速度:毫秒每格  : 毫秒/(行/格)
     */
    void setGameSpeed(int intervalMillSecondTime);

    /**
     * 消除行时候操作
     *
     * @param lines 行位置
     */
    void onRemoveLine(int lines);

    /**
     * 落下执行
     */
    void onBlocksFalled();

    /**
     * 行数用尽
     */
    void onLineFull();

    /**
     * 每游戏时钟就执行
     */
    void onTimer();

    /**
     * 左移动
     */
    void turnLeft();

    /**
     * 右边移动
     */
    void turnRight();

}
