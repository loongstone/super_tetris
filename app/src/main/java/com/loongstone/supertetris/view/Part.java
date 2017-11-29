package com.loongstone.supertetris.view;

/**
 * @author loongstone
 * @date 2017/11/29
 */
public class Part {
    public static final int DIR_0 = 0;
    public static final int DIR_90 = 1;
    public static final int DIR_180 = 2;
    public static final int DIR_270 = 3;
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
    static boolean[][][] test = {PART_A, PART_B, PART_C, PART_D};
    static final String TAG = "Tetris";

    public int getWidth() {
        if (part != null) {
            return part.length;
        }
        return 0;
    }

    public int getHeight() {
        if (part != null) {
            return part[0].length;
        }
        return 0;
    }

    public boolean[][] part;
    /**
     * 底部位置
     */
    public int bottomIndex;
    /**
     * 左边位置
     */
    public int leftIndex;
    /**
     * 旋转方向
     */
    public int direction;

    /**
     * @param originX 数组中的x下标记
     * @param originY 数组中的y下标记
     * @return 变换之后应该在画布中出现的位置, 是xy混合的位置, 需要根据相应方法解包求出x, y
     */
    public int transformPosition(int originX, int originY) {
        int x = 0;
        int y = 0;
        switch (direction) {
            case DIR_0:
                y = bottomIndex - originY;
                x = leftIndex + originX;
                break;
            case DIR_90:
                x = leftIndex + (originY);
                y = bottomIndex - (getWidth() - originX - 1);
                break;
            case DIR_180:
                y = bottomIndex - (getHeight() - originY - 1);
                x = leftIndex + (getWidth() - originX - 1);
                break;
            case DIR_270:
                x = leftIndex + (getHeight() - originY - 1);
                y = bottomIndex - originX;
                break;
            default:
        }
        return mixtureXY(x, y);
    }

    private static int mixtureXY(int x, int y) {
        if (x < 0 || y < 0) {
            return -1;
        }
        return x * 1000 + y;
    }

    public static int getXFromMixturePosition(int originX) {
        if (originX < 0) {
            return originX;
        }
        return originX / 1000;
    }

    public static int getYFromMixturePosition(int originY) {
        if (originY < 0) {
            return originY;
        }
        return originY % 1000;
    }
}
