package com.wjx.android.clearscreen;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：wangjianxiong
 * 创建时间：2021/4/27
 */
public class ClearScreenLayout extends ViewGroup {

    private final ViewDragHelper mViewDragHelper;
    private List<DragListener> mListeners;
    private int mDragState;
    private float mLastMotionX;
    private float mLastMotionY;
    private int touchSlop;
    /**
     * Indicates that any drawers are in an idle, settled state. No animation is in progress.
     */
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;

    /**
     * Indicates that a drawer is currently being dragged by the user.
     */
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;

    /**
     * Indicates that a drawer is in the process of settling to a final position.
     */
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400;

    @IntDef({STATE_IDLE, STATE_DRAGGING, STATE_SETTLING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    public interface DragListener {

        /**
         * 当遮罩层拖动时调用
         *
         * @param dragView    被拖动的View
         * @param slideOffset 滑动的偏移量
         */
        void onDragging(@NonNull View dragView, float slideOffset);

        /**
         * 当遮罩层被拖动至屏幕外时调用
         *
         * @param dragView 被拖动的View
         */
        void onDragToOut(@NonNull View dragView);

        /**
         * 当遮罩层被拖动至屏幕内时调用
         *
         * @param dragView 被拖动的View
         */
        void onDragToIn(@NonNull View dragView);

        /**
         * 当拖动状态改变时回调
         *
         * @param newState 被拖动的View
         */
        void onDragStateChanged(@State int newState);
    }

    public ClearScreenLayout(Context context) {
        this(context, null);
    }

    public ClearScreenLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClearScreenLayout);
        float sensitivity = a.getFloat(R.styleable.ClearScreenLayout_touch_slop_sensitivity, 2.0f);
        a.recycle();
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        ViewDragCallback viewDragCallback = new ViewDragCallback();
        mViewDragHelper = ViewDragHelper.create(this, sensitivity, viewDragCallback);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mViewDragHelper.setMinVelocity(minVel);
        touchSlop = mViewDragHelper.getTouchSlop();
        viewDragCallback.setDragger(mViewDragHelper);
    }

    /**
     * 添加拖拽的添加事件
     *
     * @param listener 在发生拖拽事件时通知的监听器。
     */
    public void addDragListener(@NonNull DragListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    /**
     * 移除拖拽的添加事件
     *
     * @param listener 在发生拖拽事件时通知的监听器。
     */
    public void removeDragListener(@NonNull DragListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            // This can happen if this method is called before the first call to addDragListener
            return;
        }
        mListeners.remove(listener);
    }

    void setDragViewOffset(View dragView, float slideOffset) {
        final LayoutParams lp = (LayoutParams) dragView.getLayoutParams();
        if (slideOffset == lp.onScreen) {
            return;
        }

        lp.onScreen = slideOffset;
        dispatchOnDragging(dragView, slideOffset);
    }

    float getDragViewOffset(View drawerView) {
        final LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
        return lp.onScreen;
    }

    public void updateDragState(int activeState, View activeView) {
        final int rightState = mViewDragHelper.getViewDragState();
        final int state;
        if (rightState == STATE_DRAGGING) {
            state = STATE_DRAGGING;
        } else if (rightState == STATE_SETTLING) {
            state = STATE_SETTLING;
        } else {
            state = STATE_IDLE;
        }

        if (activeView != null && activeState == STATE_IDLE) {
            final LayoutParams lp = (LayoutParams) activeView.getLayoutParams();
            Log.e("updateDragState", "lp.onScreen:" + lp.onScreen);
            if (lp.onScreen == 0) {
                dispatchDragToOutState(activeView);
            } else if (lp.onScreen == 1) {
                dispatchDragToInState(activeView);
            }
        }

        if (state != mDragState) {
            mDragState = state;

            if (mListeners != null) {
                // Notify the listeners. Do that from the end of the list so that if a listener
                // removes itself as the result of being called, it won't mess up with our iteration
                int listenerCount = mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    mListeners.get(i).onDragStateChanged(state);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int contentWidth = 0;
        int contentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            contentHeight = Math.max(contentHeight, child.getMeasuredHeight());
            contentWidth = Math.max(contentWidth, child.getMeasuredWidth());
        }
        setMeasuredDimension(measureSize(widthMeasureSpec, contentWidth + getPaddingStart() + getPaddingEnd()),
                measureSize(heightMeasureSpec, contentHeight + getPaddingTop() + getPaddingBottom()));
    }

    private int measureSize(int measureSpec, int size) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(size, specSize);
            } else {
                result = size;
            }
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.leftMargin, lp.topMargin,
                    lp.leftMargin + child.getMeasuredWidth(),
                    lp.topMargin + child.getMeasuredHeight());
        }
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                final float xDiff = Math.abs(x - mLastMotionX);
                float y = event.getY();
                final float yDiff = Math.abs(y - mLastMotionY);
                Log.v("onTouchEvent", "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                if (xDiff < touchSlop && xDiff > yDiff) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams
                ? new LayoutParams((LayoutParams) p)
                : p instanceof ViewGroup.MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    View findDraggerChild() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (((LayoutParams) child.getLayoutParams()).isDrag) {
                return child;
            }
        }
        return null;
    }

    void dispatchDragToOutState(View dragView) {
        ((LayoutParams) dragView.getLayoutParams()).isSlideOut = true;
        if (mListeners != null) {
            // 发出通知。从列表的末尾开始，这样，如果监听器由于被调用而将自己删除，则不会干扰我们的迭代
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onDragToOut(dragView);
            }
        }
    }

    void dispatchDragToInState(View dragView) {
        ((LayoutParams) dragView.getLayoutParams()).isSlideOut = false;
        if (mListeners != null) {
            // 发出通知。从列表的末尾开始，这样，如果监听器由于被调用而将自己删除，则不会干扰我们的迭代
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onDragToIn(dragView);
            }
        }
    }

    void dispatchOnDragging(View dragView, float slideOffset) {
        if (mListeners != null) {
            // 发出通知。从列表的末尾开始，这样，如果监听器由于被调用而将自己删除，则不会干扰我们的迭代
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onDragging(dragView, slideOffset);
            }
        }
    }

    boolean isSlideOut(View dragView) {
        return ((LayoutParams) dragView.getLayoutParams()).isSlideOut;
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        private ViewDragHelper mDragger;

        public void setDragger(ViewDragHelper dragger) {
            mDragger = dragger;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            return layoutParams.isDrag;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            final float offset = getDragViewOffset(releasedChild);
            final int childWidth = releasedChild.getWidth();
            int childLeft = releasedChild.getLeft();
            final int width = getWidth();
            //int left = xvel < 0 || (xvel == 0 && offset > 0.3f) ? width - childWidth : width;
            if (isSlideOut(releasedChild)) {
                slideInOrOut(releasedChild, childLeft, getWidth() * 4 / 5);
            } else {
                slideInOrOut(releasedChild, childLeft, getWidth() / 5);
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int paddingStart = getPaddingStart();
            return Math.max(left, paddingStart);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return child.getTop();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                View child = findDraggerChild();
                mDragger.captureChildView(child, pointerId);
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            final int childWidth = changedView.getWidth();
            final int width = getWidth();
            float offset = (float) (width - left) / childWidth;
            if (offset >= 0) {
                setDragViewOffset(changedView, offset);
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            updateDragState(state, mDragger.getCapturedView());
        }

        private void slideInOrOut(View releasedChild, int finalLeft, int criticalValue) {
            if (finalLeft > criticalValue) {
                mDragger.settleCapturedViewAt(getWidth(), 0);
            } else {
                mDragger.settleCapturedViewAt(((LayoutParams) releasedChild.getLayoutParams()).getMarginStart(), 0);
            }
            invalidate();
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean isDrag;
        public boolean isSlideOut;
        public float onScreen;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = null;
            try {
                a = c.obtainStyledAttributes(attrs, R.styleable.ClearScreenLayout_Layout);
                isDrag = a.getBoolean(
                        R.styleable.ClearScreenLayout_Layout_layout_dragEnable,
                        false
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

    }
}
