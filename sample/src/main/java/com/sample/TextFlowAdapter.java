package com.sample;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chrischeng.flowlayout.FlowAdapter;

import java.util.List;

public class TextFlowAdapter extends FlowAdapter<String> {

    public TextFlowAdapter(List<String> resIds) {
        super(resIds);
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setBackgroundResource(R.drawable.bg_text);
        textView.setText(mDatas.get(position));
        textView.setTextSize(18f);
        return textView;
    }
}
