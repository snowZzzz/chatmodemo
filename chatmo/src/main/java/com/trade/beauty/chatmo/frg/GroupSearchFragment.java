package com.trade.beauty.chatmo.frg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;

import com.trade.beauty.chatmo.act.GroupSearchActivity;

import com.trade.beauty.chatmo.adapter.GroupPublicListAdapter;
import com.trade.beauty.chatmo.base.BaseFragment;
import com.trade.beauty.mylibrary.R;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/19
 */
public class GroupSearchFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mFriendAdapter;
    private List<EMGroupInfo> groupsList;


    @Override
    protected void onCreateV(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_group_frgment;
    }

    @Override
    protected void initView() {
        mRecyclerView = fragmentView.findViewById(R.id.recyclerView);
    }

    @Override
    protected void initData() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFriendAdapter = new GroupPublicListAdapter(R.layout.layout_group_public_item);
        mFriendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mFriendAdapter);

        Task.callInBackground(new Callable<List<EMGroupInfo>>() {
            @Override
            public List<EMGroupInfo> call() throws Exception {
                try {
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(20, "");
                    groupsList = result.getData();
                } catch (Exception e) {
                    e.getMessage();
                }
                return groupsList;
            }
        }).continueWith(new Continuation<List<EMGroupInfo>, Object>() {
            @Override
            public Object then(Task<List<EMGroupInfo>> task) throws Exception {
                if (groupsList != null && groupsList.size() > 0) {
                    mFriendAdapter.setNewData(groupsList);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    @Override
    protected void initEvent() {
        fragmentView.findViewById(R.id.btn_search).setOnClickListener(this);
        mFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (groupsList != null && groupsList.size() > 0) {
                    EMGroupInfo emGroupInfo = groupsList.get(position);
                    ((GroupSearchActivity) (getActivity())).changeFrg(emGroupInfo);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_search) {
            Toast.makeText(getActivity(), "查找群", Toast.LENGTH_LONG).show();
        }
    }

}
