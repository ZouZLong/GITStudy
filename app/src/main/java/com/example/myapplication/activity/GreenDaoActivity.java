package com.example.myapplication.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityGreendaoBinding;
import com.example.myapplication.greendao.DbController;
import com.example.myapplication.greendao.InfoBean;

import java.util.List;

/**
 * 操作数据库的页面
 */
public class GreenDaoActivity extends BaseActivity {

    private ActivityGreendaoBinding activityGreendaoBinding;
    private DbController mDbController;
    private InfoBean infoBean1, infoBean2, infoBean3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGreendaoBinding = DataBindingUtil.setContentView(this, R.layout.activity_greendao);
        activityGreendaoBinding.setLifecycleOwner(this);
        mDbController = DbController.getInstance(this);
        init();
    }

    public void init() {
        infoBean1 = new InfoBean(null, "001", "李林丹", "女");
        infoBean2 = new InfoBean(null, "002", "王林嫣", "女");
        infoBean3 = new InfoBean(null, "003", "欧阳娜", "女");
    }


    public class OnClick_GreenDao {
        public void add() {
            //添加一条数据
            mDbController.insertOrReplace(infoBean1);
            mDbController.insertOrReplace(infoBean2);
            mDbController.insertOrReplace(infoBean3);
            showDataList();
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
        activityGreendaoBinding.dataText.setText(sb.toString());
    }
}
