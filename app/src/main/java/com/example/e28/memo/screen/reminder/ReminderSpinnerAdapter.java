package com.example.e28.memo.screen.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e28.memo.R;

import java.util.ArrayList;

/**
 * Created by User on 2019/10/18.
 */

public class ReminderSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private String[][] mDataList;
    private int mType; // 0 = 日, 1 = 時間, 2 = リピート


    public ReminderSpinnerAdapter(Context context) {
        super();
        mContext = context;
        mDataList = null;
    }

    public void setData(String[][] data, int type) {
        mDataList = data;
        mType = type;
    }

    @Override
    public int getCount() {
        return mDataList.length;
    }

    @Override
    public Object getItem(int position) {
        return mDataList[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textViewTimeName;
        TextView textViewTime;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.reminder_spinner_selected_item, null);
            switch (mType) {
                case 0: // Spinnerで日を指定
                    textViewTimeName = convertView.findViewById(R.id);
                    textViewTimeName.setText(mDataList(position).get(0));
                    textViewTime = convertView.findViewById(R.id);
                    textViewTime.setText(mDataList(position).get(1));
                    break;
                case 1: // Spinnerで時間を指定


                    break;

                case 2: // Spinnerでリピートを指定


                    break;

            }


        }
        String text = mDataList.get(position);
        TextView tv = (TextView)convertView.
                findViewById(R.id.sample_selected_text_id);
        tv.setText(text);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textViewTimeName;
        TextView textViewTime;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.reminder_spinner_selected_item, null);
            switch (mType) {
                case 0: // Spinnerで日を指定
                    textViewTimeName = convertView.findViewById(R.id);
                    textViewTimeName.setText(mDataList(position).get(0));
                    textViewTime = convertView.findViewById(R.id);
                    textViewTime.setText(mDataList(position).get(1));
                    break;
                case 1: // Spinnerで時間を指定


                    break;

                case 2: // Spinnerでリピートを指定


                    break;

            }

        return convertView;
    }
}
