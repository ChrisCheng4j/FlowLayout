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

import java.util.LinkedHashSet;

public class FlowLayout extends ViewGroup {

    private static final float DEFAULT_MARGIN = 12f;
    private static final String BUNDLE_KEY_STATE = "savedInstance";
    private static final String BUNDLE_KEY_SELECTS = "selects";

    private float horizontalMargin;
    private float verticalMargin;
    private int maxSelectedNum;
    private LinkedHashSet<Integer> selectedPos;
    protected OnStateChangedListener listener;
    private MotionEvent motionEvent;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        initAttrs(context, attrs);
        initFields();
    }

    public void setStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    public void setMaxSelectedNum(int max) {
        maxSelectedNum = max;
    }

    public void setAllState(boolean isSelected) {
        int count = getChildCount();

        if (isSelected) {
            for (int i = 0; i < count; i++) {
                selectedPos.add(i);
                getChildAt(i).setSelected(true);
            }
        } else {
            selectedPos.clear();
            for (int i = 0; i < count; i++)
                getChildAt(i).setSelected(false);
        }
    }

    public LinkedHashSet<Integer> getSelectedPos() {
        return selectedPos;
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
                boolean preState = child.isSelected();

                if (preState) {
                    selectedPos.remove(pos);
                    child.setSelected(false);
                } else {
                    if (selectedPos.size() != maxSelectedNum) {
                        selectedPos.add(pos);
                        child.setSelected(true);
                    } else if (listener != null)
                        listener.onMaxNumSelected();
                }
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

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_STATE, super.onSaveInstanceState());
        bundle.putSerializable(BUNDLE_KEY_SELECTS, selectedPos);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            selectedPos = (LinkedHashSet<Integer>) bundle.getSerializable(BUNDLE_KEY_SELECTS);
            if (selectedPos != null) {
                int size = selectedPos.size();
                if (size > 0) {
                    for (int i : selectedPos) {
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
        maxSelectedNum = a.getInteger(R.styleable.FlowLayout_maxSelectedNum, Constants.INVALID_RESULT);
        a.recycle();
    }

    private void initFields() {
        selectedPos = new LinkedHashSet<>();
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
