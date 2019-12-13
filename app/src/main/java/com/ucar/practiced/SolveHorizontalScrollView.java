package com.ucar.practiced;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * @author kaiwen.wu
 * @date 2019/11/27 13:02
 */
public class SolveHorizontalScrollView extends HorizontalScrollView {
    public SolveHorizontalScrollView(Context context) {
        super(context);
    }

    public SolveHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SolveHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
