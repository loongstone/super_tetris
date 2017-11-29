package com.loongstone.supertetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.loongstone.supertetris.view.Part;

/**
 * @author loongstone
 * @date 2017/11/26
 */

public class TetrisView extends View {
    public static final int MAX_CELL_COUNT = 50;
    private static final String TAG = "dd";
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
    private int mWidth;
    private int mHeight;
    private int mLeft;
    private int mTop;
    private boolean[][] mPoints;
    private int x;
    private int y;
    private Bitmap mBlockBitmap;
    private boolean mTouchMovieFlag = false;
    private int smallLineWidth;
    private int bigLineWidth;
    private int mBlockBitMapSize = 0;

    public TetrisView(Context context) {
        super(context);
        init();
    }

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.YELLOW);
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //根据View尺寸计算其他所需尺寸
        mWidth = getWidth();
        mHeight = getHeight();
        mCellSize = mWidth / mCellCountX;
        mCellCountY = mHeight / mCellSize;
        mLeft = (mWidth - mCellCountX * mCellSize) / 2;
        mTop = (mHeight - mCellCountY * mCellSize) / 2;
        //线条宽度
        smallLineWidth = mCellSize / 20;
        bigLineWidth = mCellSize / 10;
        //生成点数组
        initPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPoints == null) {
            initPoints();
        }
        //绘制大矩形框
        mLinePaint.setStrokeWidth(bigLineWidth);
        canvas.drawRect(mLeft, mTop, mWidth - mLeft, mHeight - mTop, mLinePaint);
        //绘制横竖线
        drawWebLines(canvas);
        //固定不变的背景,如画布,通过bitmap来简化和提高效率  canvas.drawBitmap()
        //drawMyRect(canvas, 5, 5, mRectPaint);
        drawPoint(canvas);
        drawPart(canvas);
    }

    /**
     * 设置移动的模块
     *
     * @param part 移动模块数组,最大4x4
     */
    public void setPart(Part part) {
        //不能比画布大
        int x = part.getWidth();
        int y = part.getHeight();
        if (part.direction == Part.DIR_90 || part.direction == Part.DIR_270) {
            x = part.getHeight();
        }
        if (y > mCellCountY || x > mCellCountX) {
            return;
        }
        mPart = part;
        //左右不能出画布范围
        if (mPart.leftIndex < 0) {
            mPart.leftIndex = 0;
        }
        if (mPart.leftIndex + x > mCellCountX) {
            mPart.leftIndex = mCellCountX - x;
        }
        //底部极限
        if (mPart.bottomIndex >= mCellCountY) {
            mPart.bottomIndex = mCellCountY - 1;
        }
    }

    private Part mPart;

    private void drawPart(Canvas canvas) {
        if (mPart == null || mPart.part == null) {
            return;
        }
        Log.d(TAG, "drawPart: button:" + mPart.bottomIndex);
        int x;
        int y;
        for (int j = 0; j < mPart.getHeight(); j++) {
            for (int i = 0; i < mPart.getWidth(); i++) {
                int newPosition = mPart.transformPosition(i, j);
                x = Part.getXFromMixturePosition(newPosition);
                y = Part.getYFromMixturePosition(newPosition);
                if (mPart.part[i][j]) {
                    if (x < 0 || y < 0 || x >= mCellCountX || y >= mCellCountY) {
                        continue;
                    }
                    Log.d(TAG, "drawPartDraw: X:" + x + "  Y:" + y);
                    drawBitMap(canvas, x, y);
                }
            }
        }
    }

    private void drawWebLines(Canvas canvas) {
        mLinePaint.setStrokeWidth(smallLineWidth);
        for (int i = 1; i < mCellCountX; i++) {
            x = mLeft + i * mCellSize;
            canvas.drawLine(x, mTop, x, mHeight - mTop, mLinePaint);
        }

        for (int i = 1; i < mCellCountY; i++) {
            y = mTop + i * mCellSize;
            canvas.drawLine(mLeft, y, mWidth - mLeft, y, mLinePaint);
        }
    }

    public void setCellCountOnWidth(int count) {
        if ((count > 0) && (count < MAX_CELL_COUNT)) {
            mCellCountX = count;
            //重新计算相关尺寸
            requestLayout();
        }
    }

    private void drawMyRect(Canvas canvas, int indexX, int indexY, Paint paint) {
        canvas.drawRect(
                mLeft + (mCellSize * indexX) + mCellSize / 20,
                mTop + (mCellSize * indexY) + mCellSize / 20,
                mLeft + (mCellSize * indexX) - mCellSize / 20 + mCellSize,
                mTop + (mCellSize * indexY) - mCellSize / 20 + mCellSize, paint);
    }

    private void drawBitMap(Canvas canvas, int indexX, int indexY) {
        initBlockBitMap();
        canvas.drawBitmap(mBlockBitmap,
                mLeft + (mCellSize * indexX) + mCellSize / 20,
                mTop + (mCellSize * indexY) + mCellSize / 20, mRectPaint);
    }

    private void initBlockBitMap() {
        mBlockBitMapSize = mCellSize - mCellSize / 10;
        if (mBlockBitmap == null || mBlockBitmap.getWidth() != mBlockBitMapSize) {
            if (mBlockBitmap != null) {
                if (!mBlockBitmap.isRecycled()) {
                    mBlockBitmap.recycle();
                }
            }
            Bitmap oldBlockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.block_green);
            mBlockBitmap = scaleBitmap(oldBlockBitmap, mBlockBitMapSize, mBlockBitMapSize);
        }
    }

    /**
     * 生成单元格状态数组
     */
    private void initPoints() {
        mPoints = new boolean[mCellCountX][mCellCountY];
        for (int i = 0; i < mCellCountX; i++) {
            for (int j = 0; j < mCellCountY; j++) {
                mPoints[i][j] = false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initPaint();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchMovieFlag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchMovieFlag = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!mTouchMovieFlag) {
                    shiftPoint(event.getX(), event.getY());
                }
                break;
            default:
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * BitMap缩放
     *
     * @param origin    需要缩放的BitMap
     * @param newWidth  需要的宽度
     * @param newHeight 需要的高度
     * @return 缩放处理后的BitMap
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // 使用后乘
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        //回收BitMap
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     * 根据单元格数组绘制单元格
     *
     * @param canvas canvas
     */
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mCellCountX; i++) {
            for (int j = 0; j < mCellCountY; j++) {
                if (mPoints[i][j]) {
                    drawBitMap(canvas, i, j);
                }
            }
        }
    }

    /**
     * 根据触摸点的x,y,计算出触摸所在的单元格的位置索引
     *
     * @param x 触摸点x坐标
     * @param y 触摸点y坐标
     */
    private void shiftPoint(float x, float y) {
        int indexX = (int) ((x - mLeft) / mCellSize);
        int indexY = (int) ((y - mTop) / mCellSize);
        if (indexX < 0 || indexY < 0) {
            return;
        }
        if (indexX < mPoints.length && indexY < mPoints[0].length) {
            mPoints[indexX][indexY] = !mPoints[indexX][indexY];
        }
        invalidate();
    }

    public void setBlockStatus(int indexX, int indexY, boolean show) {
        if (mPoints != null) {
            //防止越界
            if (mPoints.length > indexX && mPoints[0].length > indexY) {
                mPoints[indexX][indexY] = show;
            }
        }
    }

    public boolean[][] getBlockPoints() {
        return mPoints;
    }

    public int getCellCountX() {
        return mCellCountX;
    }

    public int getCellCountY() {
        return mCellCountY;
    }
}
