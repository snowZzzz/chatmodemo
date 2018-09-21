package com.trade.beauty.chatmo.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMGroup;


import com.trade.beauty.chatmo.adapter.FriendListAdapter;

import com.trade.beauty.chatmo.base.BaseActivity;
import com.trade.beauty.chatmo.bean.FriendBean;
import com.trade.beauty.chatmo.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class GroupMembersActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter friendAdapter;
    private String groupId;
    private EMGroup group;

    private List<String> memberList = Collections.synchronizedList(new ArrayList<String>());
    private List<FriendBean> frindLists = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recylerView);
    }

    @Override
    protected void initData() {
        groupId = getIntent().getStringExtra("groupId");
        group = EasyUtil.getEmManager().getGroup(groupId);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        friendAdapter = new FriendListAdapter(R.layout.layout_friends_item);
        friendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(friendAdapter);
    }

    @Override
    protected void initEvent() {
        Task.callInBackground(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                try {
                    memberList.clear();
                    memberList = EasyUtil.getEmManager().getGroupMembers(groupId);
                } catch (Exception e) {
                    e.getMessage();
                }
                return memberList;
            }
        }).continueWith(new Continuation<List<String>, Object>() {
            @Override
            public Object then(Task<List<String>> task) throws Exception {
                if (memberList != null && memberList.size() > 0) {
                    for (String str : memberList) {
                        frindLists.add(new FriendBean(str, false));
                    }
                }
                friendAdapter.setNewData(frindLists);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
