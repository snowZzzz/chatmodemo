package com.trade.beauty.chatmo.frg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import com.hyphenate.exceptions.HyphenateException;
import com.trade.beauty.chatmo.base.BaseFragment;
import com.trade.beauty.chatmo.easyutils.EasyUtil;
import com.trade.beauty.chatmo.utils.ToastUtils;
import com.trade.beauty.mylibrary.R;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/19
 */
public class GroupDetailFrg extends BaseFragment {
    private TextView mTvGroupName;
    private TextView mTvGroupOwner;
    private TextView mEditReason;
    private TextView mTvGroupDesc;
    private Button mBtnJoin;

    private EMGroupInfo info;
    public static EMGroup searchedGroup;

    public static GroupDetailFrg getInstance(EMGroupInfo info) {
        GroupDetailFrg groupDetailFrg = new GroupDetailFrg();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        groupDetailFrg.setArguments(bundle);
        return groupDetailFrg;
    }

    @Override
    protected void onCreateV(Bundle savedInstanceState) {
        info = (EMGroupInfo) getArguments().getSerializable("info");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_group_detail_frgment;
    }

    @Override
    protected void initView() {
        mTvGroupName = fragmentView.findViewById(R.id.tv_group_name);
        mTvGroupOwner = fragmentView.findViewById(R.id.tv_group_owner);
        mEditReason = fragmentView.findViewById(R.id.edit_reason);
        mTvGroupDesc = fragmentView.findViewById(R.id.tv_group_desc);
        mBtnJoin = fragmentView.findViewById(R.id.btn_join);
    }

    @Override
    protected void initData() {
        if (info != null) {
            Task.callInBackground(new Callable<EMGroup>() {
                @Override
                public EMGroup call() throws Exception {
                    try {
                        searchedGroup = EasyUtil.getEmManager().getGroupFromServer(info.getGroupId(), false);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    return searchedGroup;
                }
            }).continueWith(new Continuation<EMGroup, Object>() {
                @Override
                public Object then(Task<EMGroup> task) throws Exception {
                    if (searchedGroup != null) {
                        mTvGroupName.setText(searchedGroup.getGroupName());
                        mTvGroupOwner.setText(searchedGroup.getOwner());
                        mTvGroupDesc.setText(searchedGroup.getDescription());
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }
    }

    @Override
    protected void initEvent() {
        mBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            EasyUtil.getEmManager().joinGroup(info.getGroupId());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ToastUtils.showCenter(getActivity(), "加入失败");
                        }
                        ToastUtils.showCenter(getActivity(), "加入成功");
                        return null;
                    }
                }).continueWith(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);

            }
        });
    }
}
