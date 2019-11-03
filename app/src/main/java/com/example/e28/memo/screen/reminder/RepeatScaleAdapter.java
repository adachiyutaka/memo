package com.example.e28.memo.screen.reminder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Repeat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2019/10/18.
 */

class RepeatSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private String[][] mDataList;
    private Calendar now;
    private Calendar calendar;
    private Repeat repeat;
    private int mType; // 0 = 日, 1 = 時間, 2 = リピート


    public RepeatSpinnerAdapter(Context context) {
        super();
        mContext = context;
        mDataList = null;
    }

    public void setList(String[][] data, int type) {
        mDataList = data;
        mType = type;
    }

    public void setTime(Calendar calendar) {
        this.calendar = calendar;
        SimpleDateFormat formatA = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String formatDate1 = formatA.format(calendar.getTime());
        Log.d(TAG, "onCreateDialog: set adapter remindTime : " + formatDate1);
    }
    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
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

    // 選択済みのSpinnerの表示を指定
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.reminder_spinner_selected_item, null);
        }

        TextView textViewSelectedTime;
        textViewSelectedTime = convertView.findViewById(R.id.text_view_selected_time);

        switch (mType) {
            case 0: // 年月日を指定するSpinner
                textViewSelectedTime.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
                break;
            case 1: // 時間を指定するSpinner
                textViewSelectedTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + "時" + String.format("%02d", calendar.get(Calendar.MINUTE)) + "分");
                break;
            case 2: // リピートを指定するSpinner
                String textRepeat = "リピートなし";
//                switch (repeat.getRepeatScale()) {
//                    case 0:
//                        textRepeat = "リピートなし";
//                        break;
//                    case 1:
//                        textRepeat = "毎日";
//                        break;
//                    case 2:
//                        textRepeat = "毎週";
//                        break;
//                    case 3:
//                        textRepeat = "毎月";
//                        break;
//                    case 4:
//                        textRepeat = "毎年";
//                        break;
//                    default:
//                        textRepeat = "リピートなし";
//                        break;
//                }
                textViewSelectedTime.setText(textRepeat);
                break;
        }

        return convertView;
    }

    // Spinnerのドロップダウンリストの表示を指定
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.reminder_spinner_item, null);
        }

        // スピナーに項目名をセットする
        TextView textViewTimeName = convertView.findViewById(R.id.text_view_time_name);
        textViewTimeName.setText(mDataList[position][0]);

        // 時間のスピナーのみ、時間もセットする
        if (mType == 1) {
            TextView textViewTime = convertView.findViewById(R.id.text_view_time);
            textViewTime.setText(mDataList[position][1]);
        }

        return convertView;
    }
}

