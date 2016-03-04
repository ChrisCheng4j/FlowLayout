package com.mic.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {

    private static final float DEFAULT_MARGIN = 12f;
    private static final String BUNDLE_KEY_STATE = "savedInstance";
    private static final String BUNDLE_KEY_SELECTS = "selects";

    private float horizontalMargin;
    private float verticalMargin;
    protected OnStateChangedListener listener;
    private MotionEvent motionEvent;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        initAttrs(context, attrs);
    }

    public void setStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
            motionEvent = MotionEvent.obtain(event);

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        if (motionEvent == null)
            return super.performClick();

        View child = findChild((int) motionEvent.getX(), (int) motionEvent.getY());

        motionEvent = null;

        if (child != null) {
            int pos = findPosByView(child);

            if (pos != Constants.INVALID_RESULT) {
                child.setSelected(!child.isSelected());

                if (listener != null)
                    listener.onStateChanged(pos, child.isSelected());
            }
        }

        return true;
    }

    protected void reset() {
        removeAllViews();
    }

    protected int dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int selfWidth = resolveSize(0, widthMeasureSpec);
        int childCount = getChildCount();

        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int lineHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            LayoutParams params = v.getLayoutParams();
            v.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width),
                    getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height));
            int childWidth = v.getMeasuredWidth();
            int childHeight = v.getMeasuredHeight();
            lineHeight = Math.max(lineHeight, childHeight);

            if (childLeft + childWidth + getPaddingRight() > selfWidth) {
                childLeft = getPaddingLeft();
                childTop += verticalMargin + lineHeight;
                lineHeight = childHeight;
            } else
                childLeft += horizontalMargin + childWidth;
        }

        setMeasuredDimension(selfWidth, resolveSize(childTop + lineHeight + getPaddingBottom(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = r - l;
        int childCount = getChildCount();

        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int lineHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);

            if (v.getVisibility() == View.GONE)
                continue;

            int childWidth = v.getMeasuredWidth();
            int childHeight = v.getMeasuredHeight();
            lineHeight = Math.max(lineHeight, childHeight);

            if (childLeft + childWidth + getPaddingRight() > w) {
                childLeft = getPaddingLeft();
                childTop += verticalMargin + lineHeight;
                lineHeight = childHeight;
            }

            v.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += horizontalMargin + childWidth;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        int count = getChildCount();
        if (count <= 0)
            return super.onSaveInstanceState();

        ArrayList<Integer> selects = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_STATE, super.onSaveInstanceState());

        for (int i = 0; i < count; i++) {
            if (getChildAt(i).isSelected())
                selects.add(i);
        }

        bundle.putIntegerArrayList(BUNDLE_KEY_SELECTS, selects);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            ArrayList<Integer> selects = bundle.getIntegerArrayList(BUNDLE_KEY_SELECTS);
            if (selects != null) {
                int size = selects.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        View v = getChildAt(i);
                        if (v != null)
                            v.setSelected(true);
                    }
                    super.onRestoreInstanceState(bundle.getParcelable(BUNDLE_KEY_STATE));
                    return;
                }
            }
        }
        super.onRestoreInstanceState(state);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        horizontalMargin = a.getDimensionPixelSize(
                R.styleable.FlowLayout_marginHorizontal, dp2px(DEFAULT_MARGIN));
        verticalMargin = a.getDimensionPixelSize(
                R.styleable.FlowLayout_marginVertical, dp2px(DEFAULT_MARGIN));
        a.recycle();
    }

    private View findChild(int x, int y) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v.getVisibility() == View.GONE)
                continue;

            Rect outRect = new Rect();
            v.getHitRect(outRect);
            if (outRect.contains(x, y)) {
                return v;
            }
        }
        return null;
    }

    private int findPosByView(View child) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            if (v == child)
                return i;
        }

        return Constants.INVALID_RESULT;
    }
}
