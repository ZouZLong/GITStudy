package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 *表情页面
 */
public class EmotionKitActivity  extends AppCompatActivity {

    LinearLayout mLlContent;
    EditText mEtContent;
    ImageView mIvEmo;
    EmotionLayout mElEmotion;

    private EmotionKeyboard mEmotionKeyboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_kit);
        initView();
        initEmotionKeyboard();
    }

    public void initView(){
        mLlContent=findViewById(R.id.mLlContent);
        mEtContent=findViewById(R.id.mEtContent);
        mIvEmo=findViewById(R.id.mIvEmo);
        mElEmotion=findViewById(R.id.elEmotion);

        mElEmotion.attachEditText(mEtContent);
        mElEmotion.setEmotionAddVisiable(false);
        mElEmotion.setEmotionSettingVisiable(false);
    }

    private void initEmotionKeyboard() {
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToContent(mLlContent);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo);
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.setEmotionLayout(mElEmotion);
    }
}
