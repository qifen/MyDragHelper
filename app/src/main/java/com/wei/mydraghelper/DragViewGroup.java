package com.wei.mydraghelper;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by weiyilin on 16/9/22.
 */

public class DragViewGroup extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mMenuView, mMainView;
    private int mWidth;

    public DragViewGroup(Context context) {
        super(context);
        initView();
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //加载完布局文件调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    //获取宽度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenuView.getMeasuredWidth();
    }

    //拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件传递给ViewDragHelper
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        //何时开始检测触摸事件
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //如果触摸的child是mMainView时检测
            return mMainView == child;
        }

        //处理垂直滑动
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        //处理水平滑动
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        //拖动结束后调用
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //手指抬起缓慢移动到指定位置
            if (mMainView.getLeft() < 500) {
                //关闭菜单
                //相当于Scroller的startScroll方法
                mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            } else {
                //打开菜单
                mViewDragHelper.smoothSlideViewTo(mMainView, 300, 0);
                ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(this);
    }
}
