package com.example.myapplication.im;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactLayout;

public class IM_MailListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_maillist);

        // 从布局文件中获取通讯录面板
        ContactLayout contactLayout = findViewById(R.id.contact_layout);
        // 通讯录面板的默认 UI 和交互初始化
        contactLayout.initDefault();
    }
}
