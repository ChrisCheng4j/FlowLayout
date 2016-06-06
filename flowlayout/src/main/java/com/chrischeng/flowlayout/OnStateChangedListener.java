package com.chrischeng.flowlayout;

public interface OnStateChangedListener {
    void onStateChanged(int pos, boolean isSelected);

    void onMaxNumSelected();
}
