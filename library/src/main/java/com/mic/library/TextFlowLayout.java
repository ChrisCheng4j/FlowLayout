package com.mic.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

public class TextFlowLayout extends FlowLayout {

    private static final float DEFAULT_TEXT_SIZE = 14f;
    private static final float DEFAULT_TEXT_PADDING_HORIZONTAL = 12f;
    private static final float DEFAULT_TEXT_PADDING_VERTICAL = 10f;

    private float mTextSize;
    private int mTextPaddingHorizontal;
    private int mTextPaddingVertical;
    private ColorStateList mTextColor;
    private int mTextBackgroundId;

    public TextFlowLayout(Context context) {
        super(context);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public void setTexts(List<String> texts) {
        reset();

        if (texts == null || texts.size() == 0)
            return;

        for (String text : texts) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            textView.setPadding(mTextPaddingHorizontal, mTextPaddingVertical, mTextPaddingHorizontal, mTextPaddingVertical);
            textView.setBackgroundResource(mTextBackgroundId);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);

            addView(textView);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mTextSize = a.getDimension(R.styleable.TextFlowLayout_textSize, DEFAULT_TEXT_SIZE);
        mTextPaddingHorizontal = (int) a.getDimension(R.styleable.TextFlowLayout_textPaddingHorizontal,
                dp2px(DEFAULT_TEXT_PADDING_HORIZONTAL));
        mTextPaddingVertical = (int) a.getDimension(R.styleable.TextFlowLayout_textPaddingVertical,
                dp2px(DEFAULT_TEXT_PADDING_VERTICAL));
        mTextColor = a.getColorStateList(R.styleable.TextFlowLayout_textColor);
        if (mTextColor == null)
            mTextColor = getResources().getColorStateList(R.color.text_state);
        mTextBackgroundId = a.getResourceId(R.styleable.TextFlowLayout_textBackground, R.drawable.text_bg_selector);
        a.recycle();
    }
}
