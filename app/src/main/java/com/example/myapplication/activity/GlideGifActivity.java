package com.example.myapplication.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.core.app.ActivityCompat;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 测试获取Gig帧数的页面
 */
public class GlideGifActivity extends BaseActivity {
    Button image_gif_button;
    Handler handler=new Handler();
    GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glidegif);
        image_gif_button = findViewById(R.id.image_gif_button);
        gifImageView = findViewById(R.id.gifImageView);

        ActivityCompat.requestPermissions(GlideGifActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},0);

        image_gif_button.setOnClickListener(v -> {
            try {
                //开始播放  最一开始是最快的
                GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.numbel);
                gifImageView.setImageDrawable(gifDrawable);
                handler.postDelayed(() -> {
                    gifDrawable.stop();
                    int currentPosition = gifDrawable.getCurrentPosition();
                    Log.e("123", "gif长度:" + currentPosition);
                }, 6000);
                //gifDrawable.seekTo(5);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("123", "e.printStackTrace():" + e.toString());
            }
        });


    }


}
