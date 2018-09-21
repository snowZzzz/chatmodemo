package com.trade.beauty.chatmo.act;

import com.trade.beauty.chatmo.base.BaseActivity;
import com.trade.beauty.chatmo.frg.ChatFragment;
import com.trade.beauty.chatmo.utils.FragmentManagerUtil;
import com.trade.beauty.mylibrary.R;

public class ChatActivity extends BaseActivity {
    FragmentManagerUtil fragmentManagerUtil;

    ChatFragment chatFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());

        fragmentManagerUtil.chAddFrag(chatFragment, "", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
