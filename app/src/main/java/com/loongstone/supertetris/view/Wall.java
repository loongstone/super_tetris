package com.loongstone.supertetris.view;

import com.loongstone.supertetris.TetrisView;

/**
 * @author loongstone
 * @date 2017/11/29
 */

public class Wall {
    public boolean[][] getPoints() {
        return points;
    }

    public void setPoints(boolean[][] points) {
        this.points = points;
    }

    private boolean[][] points;

    public boolean getPoint(int x, int y) {
        if (points != null) {
            return points[y][x];
        }
        return false;
    }

    public void setPoint(int x, int y, boolean status) {
        if (points != null) {
            points[y][x] = status;
        }
    }

    public int getWidth() {
        if (points != null && points[0] != null) {
            return points[0].length;
        }
        return 0;
    }

    public int getHeight() {
        if (points != null) {
            return points.length;
        }
        return 0;
    }

    public Wall(int width, int height) {
        points = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                points[i][j] = false;
            }
        }
    }

    public void clear() {
        if (points == null) {
            return;
        }
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                points[i][j] = false;
            }
        }
    }

    public void clearLine(int x) {
        for (int i = 0; i < getWidth(); i++) {
            points[x][i] = false;
        }
    }

    public void reversePoint(int x, int y) {
        points[y][x] = !points[y][x];
    }

    public void exchangeLine(int x1, int x2) {
        boolean[] tmp;
        tmp = points[x1];
        points[x1] = points[x2];
        points[x2] = tmp;
    }
}
