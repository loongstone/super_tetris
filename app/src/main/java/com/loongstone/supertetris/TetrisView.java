package com.loongstone.supertetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author loongstone
 * @date 2017/11/26
 */

public class TetrisView extends View {
    /**
     * 单元格大小:长宽相等
     */
    private int mCellSize = 70;
    /**
     * 横向单元格数量
     */
    private int mCellCountX = 12;
    /**
     * 纵向单元格 数量
     */
    private int mCellCountY;
    private Paint mLinePaint;
    private Paint mRectPaint;
    private int width;
    private int height;
    private int left;
    private int top;
    private boolean[][] points;
    int x, y;

    public TetrisView(Context context) {
        super(context);
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.YELLOW);
        mLinePaint.setStrokeWidth(5);
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.BLUE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        mCellSize = width / mCellCountX;
        mCellCountY = height / mCellSize;
        left = (width - mCellCountX * mCellSize) / 2;
        top = (height - mCellCountY * mCellSize) / 2;

        mLinePaint.setStrokeWidth(mCellSize / 20);
        if (points == null) {
            points = new boolean[mCellCountX][mCellCountY];
        }
        //绘制竖线
        for (int i = 0; i <= mCellCountX; i++) {
            x = left + i * mCellSize;
            if (i == 0 || i == mCellCountX) {
                mLinePaint.setStrokeWidth(mCellSize / 10);
            } else {
                mLinePaint.setStrokeWidth(mCellSize / 20);
            }
            canvas.drawLine(x, top, x, height - top, mLinePaint);
        }

        for (int i = 0; i <= mCellCountY; i++) {
            y = top + i * mCellSize;
            if (i == 0 || i == mCellCountY) {
                mLinePaint.setStrokeWidth(mCellSize / 10);
            } else {
                mLinePaint.setStrokeWidth(mCellSize / 20);
            }
            canvas.drawLine(left, y, width - left, y, mLinePaint);
        }
        canvas.drawRect(left, top, left + mCellSize, top + mCellSize, mRectPaint);
        //固定不变的背景,如画布,通过bitmap来简化和提高效率  canvas.drawBitmap();
    }

    public void setCellSize(int size) {

    }
}
