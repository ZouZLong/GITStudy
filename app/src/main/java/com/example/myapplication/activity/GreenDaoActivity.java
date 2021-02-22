package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.greendao.DbController;
import com.example.myapplication.greendao.InfoBean;

import java.util.List;

/**
 * 操作数据库的页面
 */
public class GreenDaoActivity extends BaseActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textView;
    private DbController mDbController;
    private InfoBean infoBean1, infoBean2, infoBean3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendao1);
        mDbController = DbController.getInstance(this);
        initView();
        init();
    }

    public void initView() {
        button1 = findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        textView = findViewById(R.id.textView);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    public void init() {
        infoBean1 = new InfoBean(null, "001", "李林丹", "女");
        infoBean2 = new InfoBean(null, "002", "王林嫣", "女");
        infoBean3 = new InfoBean(null, "003", "欧阳娜", "女");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1://添加数据
                mDbController.insertOrReplace(infoBean1);
                mDbController.insertOrReplace(infoBean2);
                mDbController.insertOrReplace(infoBean3);
                showDataList();
                break;
            case R.id.button2://删除数据
                mDbController.delete("王林嫣");
                showDataList();
                break;
            case R.id.button3://更新数据
                mDbController.update(infoBean1);
                showDataList();
                break;
        }
    }


    private void showDataList() {
        StringBuilder sb = new StringBuilder();
        List<InfoBean> infoBeanList = mDbController.searchAll();
        for (InfoBean personInfor : infoBeanList) {
            sb.append("id:").append(personInfor.getId())
                    .append("perNo:").append(personInfor.getPerNo())
                    .append("name:").append(personInfor.getName())
                    .append("sex:").append(personInfor.getSex())
                    .append("\n");
        }
        textView.setText(sb.toString());
    }
}
