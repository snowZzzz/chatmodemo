package com.trade.beauty.chatmo.frg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.trade.beauty.chatmo.Constants;
import com.trade.beauty.chatmo.act.GroupMembersActivity;
import com.trade.beauty.chatmo.bean.Constant;
import com.trade.beauty.chatmo.easyutils.emlisenter.MyEMCallBack;
import com.trade.beauty.chatmo.adapter.ChatAdapter;
import com.trade.beauty.chatmo.base.BaseFragment;
import com.trade.beauty.chatmo.bean.ChatModel;
import com.trade.beauty.chatmo.bean.ItemModel;
import com.trade.beauty.chatmo.easyutils.EaseNotifier;
import com.trade.beauty.chatmo.easyutils.EasyUtil;
import com.trade.beauty.chatmo.easyutils.emlisenter.MyEMMessageListener;
import com.trade.beauty.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/20
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {
    private EditText mInputEdit;
    private TextView mSendBtn;
    private TextView mTvAdd;
    private LinearLayout mLayoutBottom;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private LinearLayout mLayoutGroupTitle;
    private Button mBtnMembers;
    private TextView mTvGroupName;

    private MyEMMessageListener myEMMessageListener;//通过注册消息监听来接收消息。
    private EaseNotifier easeNotifier;
    private EMConversation mConversation; // 当前会话对象

    private String mChatId;   // 当前聊天的 ID
    private int mChatType;//当前聊天类型
    private String mGroupName;//当前聊天的群名

    private String currUsername;
    private boolean isShowBottom = false;
    ArrayList<ItemModel> models;

    @Override
    protected void onCreateV(Bundle savedInstanceState) {
        createMsgLisenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        easeNotifier = new EaseNotifier(getActivity());
        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除消息监听
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        mInputEdit = fragmentView.findViewById(R.id.et);
        mSendBtn = fragmentView.findViewById(R.id.tvSend);
        mTvAdd = fragmentView.findViewById(R.id.tvAdd);
        mLayoutBottom = fragmentView.findViewById(R.id.layout_bottom);
        mRecyclerView = fragmentView.findViewById(R.id.recylerView);
        mLayoutGroupTitle = fragmentView.findViewById(R.id.layout_group_title);
        mBtnMembers = fragmentView.findViewById(R.id.btn_members);
        mTvGroupName = fragmentView.findViewById(R.id.tv_group_name);
    }

    @Override
    protected void initData() {
        models = new ArrayList<>();
        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getArguments().getString(Constants.EXTRA_USER_ID);
        mChatType = getArguments().getInt(Constants.EXTRA_CHAT_TYPE);
        currUsername = EMClient.getInstance().getCurrentUser();
        mAdapter = new ChatAdapter();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (Constants.CHATTYPE_GROUP == mChatType) {
            mLayoutGroupTitle.setVisibility(View.VISIBLE);
            mGroupName = getArguments().getString(Constants.EXTRA_GROUP_NAME);
            mTvGroupName.setText(mGroupName);
        }

        initConversation();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSend) {
            sendMessage();
        } else if (i == R.id.tvAdd) {
            isShowBottom = !isShowBottom;
            mLayoutBottom.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
            if (isShowBottom) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in);
                LayoutAnimationController controller = new LayoutAnimationController(animation);
                mLayoutBottom.setLayoutAnimation(controller);
            } else {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out);
                LayoutAnimationController controller = new LayoutAnimationController(animation);
                mLayoutBottom.setLayoutAnimation(controller);
            }
        } else if (i == R.id.btn_members) {
            if (Constants.CHATTYPE_GROUP == mChatType) {
            Intent intent = new Intent(getActivity(), GroupMembersActivity.class);
            intent.putExtra("groupId", mChatId);
            startActivity(intent);
            }
        }
    }

    @Override
    protected void initEvent() {
        mSendBtn.setOnClickListener(this);
        mTvAdd.setOnClickListener(this);
        mBtnMembers.setOnClickListener(this);
    }

    private void sendMessage() {
        String content = mInputEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            mInputEdit.setText("");
            // 创建一条新消息，第一个参数为消息内容，第二个为接受者username:为对方用户或者群聊的id
            EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(content, mChatId);
            if (Constants.CHATTYPE_GROUP == mChatType) {
                message.setChatType(EMMessage.ChatType.GroupChat);    //如果是群聊，设置chattype，默认是单聊
            }
            ArrayList<ItemModel> newLists = new ArrayList<>();
            ChatModel model = new ChatModel();
            model.setContent(content);
            model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
            newLists.add(new ItemModel(ItemModel.CHAT_B, model));
            mAdapter.addAll(newLists);

            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

            hideKeyBorad(mInputEdit);

            // 调用发送消息的方法
            EasyUtil.getEmManager().sendMessage(message);
            // 为消息设置回调
            message.setMessageStatusCallback(new MyEMCallBack() {
                @Override
                public void onProgress(int i, String s) {
                    // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                }
            });
        }
    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public void createMsgLisenter() {
        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // 循环遍历当前收到的消息
                for (EMMessage message : messages) {
                    Log.i("yyl", "收到新消息:" + message);
                    if (message.getFrom().equals(mChatId)) {
                        // 设置消息为已读
                        mConversation.markMessageAsRead(message.getMsgId());
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = message;
                        mHandler.sendMessage(msg);
                    } else {
                        // TODO 如果消息不是当前会话的消息发送通知栏通知
                        easeNotifier.notify(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                super.onCmdMessageReceived(messages);
                for (int i = 0; i < messages.size(); i++) {
                    // 透传消息
                    EMMessage cmdMessage = messages.get(i);
                    EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                    Log.i("yyl", "收到 CMD 透传消息" + body.action());
                }
            }
        };
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    public void initConversation() {
        mConversation = EasyUtil.getEmManager().getConversation(mChatId, null, true);//第三个表示如果会话不存在是否创建
        mConversation.markAllMessagesAsRead();        // 设置当前会话未读数为 0
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            List<EMMessage> emMessages = mConversation.getAllMessages();
            for (EMMessage message : emMessages) {
                ChatModel model = new ChatModel();
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                if (!message.getFrom().equals(currUsername)) {
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_A, model));
                } else {
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_B, model));
                }
                mAdapter.replaceAll(models);
                mRecyclerView.scrollToPosition(models.size() - 1);
            }
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();

                    ArrayList<ItemModel> newLists = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
                    newLists.add(new ItemModel(ItemModel.CHAT_A, model));
                    mAdapter.addAll(newLists);
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                    break;
            }
        }
    };


}
