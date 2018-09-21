package com.trade.beauty.chatmo.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trade.beauty.chatmo.base.BaseActivity;
import com.trade.beauty.chatmo.frg.FriendListFrg;
import com.trade.beauty.chatmo.utils.FragmentManagerUtil;
import com.trade.beauty.mylibrary.R;

import java.util.List;


public class FriendListActivity extends BaseActivity {
    FragmentManagerUtil fragmentManagerUtil;
    FriendListFrg friendListFrg;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_friend_list;
    }

    @Override
    protected void initView() {
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        friendListFrg = new FriendListFrg();

        fragmentManagerUtil.chAddFrag(friendListFrg, "", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    public void over(List<String> vars) {
        setResult(RESULT_OK, new Intent().putExtra("newmembers", vars.toArray(new String[vars.size()])));
        finish();
    }
}
