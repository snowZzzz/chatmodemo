package com.trade.beauty.chatmo.frg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.trade.beauty.chatmo.Constants;
import com.trade.beauty.chatmo.act.ChatActivity;
import com.trade.beauty.chatmo.act.FriendListActivity;
import com.trade.beauty.chatmo.adapter.FriendListAdapter;
import com.trade.beauty.chatmo.base.BaseFragment;
import com.trade.beauty.chatmo.bean.FriendBean;
import com.trade.beauty.chatmo.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/21
 */
public class FriendListFrg extends BaseFragment {
    private RecyclerView mRecyclerView;
    private Button mBtnSure;

    private BaseQuickAdapter friendAdapter;
    List<String> usernames = null;
    List<FriendBean> frindLists = null;


    @Override
    protected void onCreateV(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_list;
    }

    @Override
    protected void initView() {
        mBtnSure = fragmentView.findViewById(R.id.btn_sure);
        mRecyclerView = fragmentView.findViewById(R.id.rv_friend_list);
    }

    @Override
    protected void initData() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        friendAdapter = new FriendListAdapter(R.layout.layout_friends_item);
        friendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(friendAdapter);

        frindLists = new ArrayList<>();

        Task.callInBackground(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                usernames = EasyUtil.getEmManager().getAllContactsFromServer();
                return usernames;
            }
        }).continueWith(new Continuation<List<String>, Object>() {
            @Override
            public Object then(Task<List<String>> task) throws Exception {
                if (usernames != null && usernames.size() > 0) {
                    for (String str : usernames) {
                        frindLists.add(new FriendBean(str, false));
                    }
                }
                friendAdapter.setNewData(frindLists);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected void initEvent() {
        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FriendBean> frinds = friendAdapter.getData();
                List<String> vars = new ArrayList<>();
                if (frinds != null && frinds.size() > 0) {
                    for (FriendBean friendBean : frinds) {
                        if (friendBean.isSelect()) {
                            vars.add(friendBean.getUserName());
                        }
                    }
                }

                vars.add(EMClient.getInstance().getCurrentUser());
                ((FriendListActivity) (getActivity())).over(vars);
            }
        });

        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID, frindLists.get(position).getUserName());
                intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                startActivity(intent);
            }
        });
    }
}
