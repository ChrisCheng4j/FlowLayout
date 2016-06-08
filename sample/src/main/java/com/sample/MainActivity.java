package com.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chrischeng.flowlayout.FlowLayout;
import com.chrischeng.flowlayout.OnStateChangedListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 30; i++)
            list.add("option_" + i);

        final FlowLayout flowLayout = (FlowLayout) findViewById(R.id.fl);
        assert flowLayout != null;
        flowLayout.setAdapter(new TextFlowAdapter(list));
        flowLayout.setMaxSelectedNum(5);
        flowLayout.setOnStateChangedListener(new OnStateChangedListener() {
            @Override
            public void onStateChanged(int pos, boolean isSelected) {
                ToastManager.getInstance().showToast(isSelected ? "select:" + pos : "unselect:" + pos);
            }

            @Override
            public void onMaxNumSelected() {
                ToastManager.getInstance().showToast("reached max");
            }
        });

        TextView selectedTextView = (TextView) findViewById(R.id.tv_selected);
        assert selectedTextView != null;
        selectedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                LinkedHashSet<Integer> selectedPos = flowLayout.getSelectedPos();
                for (Integer i : selectedPos) {
                    sb.append(i);
                    sb.append("  ");
                }

                ToastManager.getInstance().showToast(sb.toString());
            }
        });
    }
}
