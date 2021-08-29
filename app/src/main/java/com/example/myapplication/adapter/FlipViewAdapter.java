package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class FlipViewAdapter extends BaseAdapter {

    private List<String> list;
    private Context mContext;

    public FlipViewAdapter(Context mContext, List<String> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_flip_view, null);
        TextView textView = view.findViewById(R.id.textFlipView);
        textView.setText(list.get(position));
        return view;
    }
}
