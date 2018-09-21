package com.trade.beauty.chatmo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangzz on 2018/8/31
 */
public abstract class BaseFragment extends Fragment {
    protected View fragmentView;
    public LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        if (null == fragmentView) {
            fragmentView = inflater.inflate(this.getLayoutId(), container, false);
            onCreateV(savedInstanceState);
        }
        initView();
        initData();
        initEvent();

        return fragmentView;
    }

    protected abstract void onCreateV(Bundle savedInstanceState);

    private void showLoadingDialog() {
    }

    private void hideLoadingDialog() {
    }

    /**
     * 设置界面layout
     */
    protected abstract int getLayoutId();

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();
}
