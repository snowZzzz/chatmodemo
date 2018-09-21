package com.trade.beauty.chatmo.act;
import com.trade.beauty.chatmo.base.BaseActivity;
import com.trade.beauty.chatmo.frg.ConversationFrg;
import com.trade.beauty.chatmo.utils.FragmentManagerUtil;
import com.trade.beauty.mylibrary.R;

public class ConversationActivity extends BaseActivity {
    FragmentManagerUtil fragmentManagerUtil;
    ConversationFrg conversationFrg;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_conversation;
    }

    @Override
    protected void initView() {
        fragmentManagerUtil = new FragmentManagerUtil(this, com.trade.beauty.mylibrary.R.id.layout_frame);
        conversationFrg = new ConversationFrg();

        fragmentManagerUtil.chAddFrag(conversationFrg, "", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


}
