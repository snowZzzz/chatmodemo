package com.trade.beauty.chatmo.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.trade.beauty.chatmo.utils.StatusBarUtil;

/**
 * Created by zhangzz on 2018/9/21
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(getLayoutResId());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initView();
        initData();
        initEvent();
    }

    //获取到布局ID
    public abstract int getLayoutResId() ;
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();

}
