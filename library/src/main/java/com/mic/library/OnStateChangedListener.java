package com.mic.library;

public interface OnStateChangedListener {
    void onStateChanged(int pos, boolean isSelected);

    void onMaxNumSelected();
}
