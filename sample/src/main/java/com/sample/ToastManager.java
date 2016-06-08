package com.sample;

import android.text.TextUtils;
import android.widget.Toast;

public class ToastManager {

    private Toast mToast;

    @SuppressWarnings("ShowToast")
    private ToastManager() {
        mToast = Toast.makeText(Application.getContext(), "", Toast.LENGTH_SHORT);
    }

    public static ToastManager getInstance() {
        return SingleHolder.instance;
    }

    public void showToast(String tip) {
        if (!TextUtils.isEmpty(tip)) {
            mToast.setText(tip);
            mToast.show();
        }
    }

    private static class SingleHolder {
        private static ToastManager instance = new ToastManager();
    }
}
