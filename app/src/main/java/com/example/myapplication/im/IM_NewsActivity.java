package com.example.myapplication.im;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;

public class IM_NewsActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_news);

        // 从布局文件中获取会话列表面板
        ConversationLayout conversationLayout = findViewById(R.id.conversation_layout);
        // 初始化聊天面板
        conversationLayout.initDefault();


        // 获取 TitleBarLayout
        TitleBarLayout titleBarLayout = conversationLayout.findViewById(R.id.conversation_title);
        // 设置标题
        titleBarLayout.setTitle("囚爱", TitleBarLayout.POSITION.MIDDLE);
        // 隐藏左侧 Group
        titleBarLayout.getLeftGroup().setVisibility(View.GONE);
        // 设置右侧的菜单图标
        titleBarLayout.setRightIcon(R.drawable.conversation_more);

    }
}
