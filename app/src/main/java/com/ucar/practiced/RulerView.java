package com.ucar.practiced;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author kaiwen.wu
 * @date 2019/11/23
 */
public class RulerView extends View {

    private Paint mPaintLine;
    private Paint mPaintText;
    private SharedPreferences.Editor mEditor;

    private int viewWidth = getViewWidth();
    private float screenHalfWidth = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
    private float pressPosition;
    float movePosition = 0;
    private float totalPosition = 0;

    /**
     * 尺子总刻度数
     */
    private static final int RULER_MAX_NUM = 100;
    /**
     * 尺子两个大刻度之间的小刻度间隔数
     */
    private static final int RULER_SMALL_BIG_NUM = 10;
    /**
     * 每条刻度的间隙
     */
    private static final float LINE_DISTANCE = 10;
    /**
     * 大刻度的垂直长度
     */
    private static final float RULER_BIG_VERTICAL_SIZE = 30;
    /**
     * 小刻度的垂直长度
     */
    private static final float RULER_SMALL_VERTICAL_SIZE = 20;
    /**
     * 每个刻度的水平宽度
     */
    private static final float RULER_HORIZONTAL_SIZE = 2;
    /**
     * 调整显示刻度数文本的垂直起点
     */
    private static final float RULER_ADJUST_VERTICAL_TEXT = 15;

    float lineDistancePixel;
    float smallVerticalPixel;
    float bigVerticalPixel;
    float rulerHorizontalPixel;
    float textVerticalPixel;

    public RulerView(Context context) {
        super(context);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setColor(Color.GRAY);

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(SizeUtils.spToPixel(14));
        //设置横坐标以字的中心为标准，纵坐标仍是字的底部
        mPaintText.setTextAlign(Paint.Align.CENTER);

        SharedPreferences mSharedPreferences = getContext().getSharedPreferences("num", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        lineDistancePixel = SizeUtils.dpToPixel(LINE_DISTANCE);
        smallVerticalPixel = SizeUtils.dpToPixel(RULER_SMALL_VERTICAL_SIZE);
        bigVerticalPixel = SizeUtils.dpToPixel(RULER_BIG_VERTICAL_SIZE);
        rulerHorizontalPixel = SizeUtils.dpToPixel(RULER_HORIZONTAL_SIZE);
        textVerticalPixel = SizeUtils.dpToPixel(RULER_ADJUST_VERTICAL_TEXT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#C8E5CC"));
        float startLine = screenHalfWidth;

        for (int i = 0; i <= RULER_MAX_NUM; i++) {
            if (i % RULER_SMALL_BIG_NUM != 0) {
                canvas.drawRect(startLine, 0,
                        startLine + rulerHorizontalPixel, smallVerticalPixel, mPaintLine);
            } else {
                canvas.drawRect(startLine, 0,
                        startLine + rulerHorizontalPixel, bigVerticalPixel, mPaintLine);
                //x是左下角的坐标时，使用paint.measureText(String.valueOf(i))获取绘制文字的宽度
                canvas.drawText(String.valueOf(i),
                        startLine, bigVerticalPixel + textVerticalPixel, mPaintText);

            }
            startLine += rulerHorizontalPixel + lineDistancePixel;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressPosition = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                movePosition = pressPosition - event.getX();
                pressPosition = event.getX();
                //滑动时当X坐标减小时测量数据是增大的
                totalPosition += movePosition;
                //在0~viewWidth范围内才能滑动
                if(totalPosition <= viewWidth + rulerHorizontalPixel && totalPosition >= 0){
                    setMeasureNum(totalPosition);
                    //若使用scrollBy()，则当滑动速度非常慢的时候移动距离和滑动距离会不相等，造成数据错误
                    scrollTo((int) (totalPosition), 0);
                }
                else{
                    //复原滑动的距离
                    totalPosition -= movePosition;
                }
                break;
            case MotionEvent.ACTION_UP:
                movePosition = pressPosition - event.getX();
                pressPosition = event.getX();
                totalPosition += movePosition;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("position", "action is cancel!");
                break;
            default:
                break;
        }
        return true;
    }


    public int getViewWidth() {
        return (int) (SizeUtils.dpToPixel(LINE_DISTANCE + RULER_HORIZONTAL_SIZE) * RULER_MAX_NUM);
    }

    public void setMeasureNum(float position) {
        //除后得到当前坐标x占整个尺子view的比例
        mEditor.putInt("measure", (int) (position * 100 / viewWidth));
        mEditor.apply();
    }


}
