package com.trade.beauty.chatmo.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.chat.EMGroupInfo;

import com.trade.beauty.chatmo.base.BaseActivity;
import com.trade.beauty.chatmo.frg.GroupDetailFrg;
import com.trade.beauty.chatmo.frg.GroupSearchFragment;
import com.trade.beauty.chatmo.utils.FragmentManagerUtil;
import com.trade.beauty.mylibrary.R;

public class GroupSearchActivity extends BaseActivity {
    FragmentManagerUtil fragmentManagerUtil;
    GroupSearchFragment groupSearchFragment;
    GroupDetailFrg groupDetailFrg;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_group_search;
    }

    @Override
    protected void initView() {
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        groupSearchFragment = new GroupSearchFragment();

        fragmentManagerUtil.chAddFrag(groupSearchFragment, "", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    public void changeFrg(EMGroupInfo info) {
        fragmentManagerUtil.chHideFrag(groupSearchFragment);
        groupDetailFrg = GroupDetailFrg.getInstance(info);
        fragmentManagerUtil.chReplaceFrag(groupDetailFrg, "", false);
    }
}
