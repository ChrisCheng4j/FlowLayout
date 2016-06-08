package com.chrischeng.flowlayout;

import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public abstract class FlowAdapter<T> {

    protected List<T> mDatas;

    public FlowAdapter() {

    }

    public FlowAdapter(List<T> datas) {
        mDatas = datas;
    }

    public FlowAdapter(T[] datas) {
        mDatas = Arrays.asList(datas);
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
    }

    public void setDatas(T[] datas) {
        mDatas = Arrays.asList(datas);
    }

    public abstract View getView(int position, ViewGroup parent);

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public T getItem(int position) {
        return getCount() > position ? mDatas.get(position) : null;
    }

    public void notifyDataSetChanged() {

    }
}
