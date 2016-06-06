package com.chrischeng.flowlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import java.util.List;

public class TextFlowLayout extends FlowLayout {

    private float mTextSize;
    private int mTextPaddingHorizontal;
    private int mTextPaddingVertical;
    private ColorStateList mTextColor;
    private int mTextBackgroundId;

    public TextFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
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

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mTextSize = a.getDimension(R.styleable.TextFlowLayout_android_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mRes.getInteger(R.integer.text_size), mRes.getDisplayMetrics()));
        mTextColor = a.getColorStateList(R.styleable.TextFlowLayout_android_textColor);
        if (mTextColor == null)
            mTextColor = getResources().getColorStateList(R.color.text_state);
        mTextPaddingHorizontal = (int) a.getDimension(R.styleable.TextFlowLayout_tfl_padding_horizontal,
                mRes.getDimension(R.dimen.text_padding));
        mTextPaddingVertical = (int) a.getDimension(R.styleable.TextFlowLayout_tfl_padding_vertical,
                mRes.getDimension(R.dimen.text_padding));
        mTextBackgroundId = a.getResourceId(R.styleable.TextFlowLayout_tfl_background, R.drawable.text_bg_selector);
        a.recycle();
    }
}
