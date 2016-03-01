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

    private float textSize;
    private int textPaddingHorizontal;
    private int textPaddingVertical;
    private ColorStateList textColor;
    private int textBackgroundId;

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
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            textView.setPadding(textPaddingHorizontal, textPaddingVertical, textPaddingHorizontal, textPaddingVertical);
            textView.setBackgroundResource(textBackgroundId);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);

            addView(textView);
        }
    }

    public void setAllChildSelected(boolean isSelected) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setSelected(isSelected);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        textSize = a.getDimension(R.styleable.TextFlowLayout_textSize, DEFAULT_TEXT_SIZE);
        textPaddingHorizontal = (int) a.getDimension(R.styleable.TextFlowLayout_textPaddingHorizontal,
                dp2px(DEFAULT_TEXT_PADDING_HORIZONTAL));
        textPaddingVertical = (int) a.getDimension(R.styleable.TextFlowLayout_textPaddingVertical,
                dp2px(DEFAULT_TEXT_PADDING_VERTICAL));
        textColor = a.getColorStateList(R.styleable.TextFlowLayout_textColor);
        if (textColor == null)
            textColor = getResources().getColorStateList(R.color.text_state);
        textBackgroundId = a.getResourceId(R.styleable.TextFlowLayout_textBackground, R.drawable.text_bg_selector);
        a.recycle();
    }
}
