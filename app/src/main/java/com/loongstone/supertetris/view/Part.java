package com.loongstone.supertetris.view;

import java.util.Random;

/**
 * @author loongstone
 * @date 2017/11/29
 */
public class Part {
    public static final int DIR_0 = 0;
    public static final int DIR_90 = 1;
    public static final int DIR_180 = 2;
    public static final int DIR_270 = 3;
    private static final int DIT_COUNT = 4;
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
    private static boolean[][][] parts = {PART_A, PART_B, PART_C, PART_D};
    private static final String TAG = "SimpleTetris";
    private static Random random = new Random();

    /**
     * 只能通过静态方法实例化
     */
    private Part() {

    }

    /**
     * 实例化并赋予随机属性
     *
     * @return 随机属性的块
     */
    public static Part getRandomPart() {
        Part part = new Part();
        //取较大的随机数,然后取余数,得到较小的随机数
        part.part = parts[random.nextInt(1000) % parts.length];
        random.setSeed(System.nanoTime());
        part.direction = random.nextInt(1000) % DIT_COUNT;
        part.bottomIndex = -1;
        return part;
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
     * 旋转方向 0-3 一共四个方向
     */
    private int direction;

    public int getDirection() {
        return direction;
    }

    /**
     * 旋转 顺时针
     */
    public void rotateDirection() {
        direction++;
        direction %= DIT_COUNT;
    }

    /**
     * 旋转 逆时针
     */
    public void reverseDirection() {
        //防止出现负数
        direction += DIT_COUNT;
        direction--;
        direction %= DIT_COUNT;
    }

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
