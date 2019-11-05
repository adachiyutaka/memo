package com.example.e28.memo.screen.reminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Paint;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Repeat;
import com.google.android.flexbox.FlexboxLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by User on 2019/10/25.
 */

public class RepeatDialogFragment extends DialogFragment {

    Context context;
    RepeatDialogFragmentListener listener;
    long repeatId;
    Repeat repeat;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    EditText intervalEditText;
    Spinner scaleSpinner;
    ArrayAdapter<CharSequence> everyAdapter;
    ArrayAdapter<CharSequence> intervalAdapter;
    Calendar now;
    String week[];
    DatePickerDialog datePickerDialog;
    EditText countEditText;

    String TAG = "mytext";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

        // 年月日表示フォーマット
        final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        // 通知終了日
        final Calendar notifyEndDate = Calendar.getInstance();
        week = new String[7];
        week[0] = "日";
        week[1] = "月";
        week[2] = "火";
        week[3] = "水";
        week[4] = "木";
        week[5] = "金";
        week[6] = "土";

        now = Calendar.getInstance();
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        final int lastDayOfMonth = now.getActualMaximum(Calendar.DATE);

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_fragment_repeat);


        repeat = new Repeat();
        repeat.setNotifyEndDate(now.getTime());
        repeat.setNotifyRemainCount(22);

        // 週ごとにリピートする場合の詳細を指定するViewを作成
        final FlexboxLayout dayOfWeekFragment = dialog.findViewById(R.id.fragment_day_of_week);
        String[] dayOfWeekStrings = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        final CheckBox checkBoxes[] = new CheckBox[7];
        for (int i = 0 ; i < dayOfWeekStrings.length ; i++) {
            final CheckBox dOWCheckBox = dialog.findViewById(getResources().getIdentifier("check_box_" + dayOfWeekStrings[i], "id", context.getPackageName()));
            dOWCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        // TODO:初期値の設定
        intervalEditText = dialog.findViewById(R.id.text_view_interval);
        int interval = (Objects.nonNull(repeat.getRepeatInterval()) && repeat.getRepeatInterval() != 0)? repeat.getRepeatInterval() : 1; // repeatに設定がない場合、初期値を1とする
        intervalEditText.setText(String.valueOf(interval), TextView.BufferType.NORMAL);
        intervalEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99", intervalEditText)});
        intervalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIntervalSpinner();
            }
        });

        // 月ごとにリピートする場合の詳細を指定するViewを作成
        final RadioButton sameDayRadioButton = dialog.findViewById(R.id.radio_button_same_day);
        final RadioButton sameDOWRadioButton = dialog.findViewById(R.id.radio_button_same_dow);
        final RadioButton sameLastRadioButton = dialog.findViewById(R.id.radio_button_same_last);
        sameDOWRadioButton.setText("その月の第" + now.get(Calendar.DAY_OF_WEEK_IN_MONTH) + week[now.get(Calendar.DAY_OF_WEEK)] + "曜日");

        // 月ごと、週ごとのViewは、初期状態ではすべて非表示
        dayOfWeekFragment.setVisibility(View.GONE);
        sameDayRadioButton.setVisibility(View.GONE);
        sameDOWRadioButton.setVisibility(View.GONE);
        sameLastRadioButton.setVisibility(View.GONE);

        //
        scaleSpinner = dialog.findViewById(R.id.spinner_scale);
        everyAdapter = ArrayAdapter.createFromResource(context, R.array.time_scale_every, android.R.layout.simple_spinner_item);
        everyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalAdapter = ArrayAdapter.createFromResource(context, R.array.time_scale, android.R.layout.simple_spinner_item);
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateIntervalSpinner();
        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 週ごと、月ごとの場合は詳細を指定するViewを可視化する
                switch (position) {
                    case 0:
                        dayOfWeekFragment.setVisibility(View.GONE);
                        sameDayRadioButton.setVisibility(View.GONE);
                        sameDOWRadioButton.setVisibility(View.GONE);
                        sameLastRadioButton.setVisibility(View.GONE);
                        break;
                    case 1:
                        dayOfWeekFragment.setVisibility(View.VISIBLE);
                        sameDayRadioButton.setVisibility(View.GONE);
                        sameDOWRadioButton.setVisibility(View.GONE);
                        sameLastRadioButton.setVisibility(View.GONE);
                        break;
                    case 2:
                        dayOfWeekFragment.setVisibility(View.GONE);
                        sameDayRadioButton.setVisibility(View.VISIBLE);
                        sameDOWRadioButton.setVisibility(View.VISIBLE);
                        if (dayOfMonth == lastDayOfMonth) {
                            sameLastRadioButton.setVisibility(View.VISIBLE);
                        } else {
                            sameLastRadioButton.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        dayOfWeekFragment.setVisibility(View.GONE);
                        sameDayRadioButton.setVisibility(View.GONE);
                        sameDOWRadioButton.setVisibility(View.GONE);
                        sameLastRadioButton.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RadioButton noEndRadioButton = dialog.findViewById(R.id.radio_button_no_end);
        RadioButton countRadioButton = dialog.findViewById(R.id.radio_button_repeat_count);
        RadioButton dateRadioButton = dialog.findViewById(R.id.radio_button_end_date);
        final RadioButton[] radioButtons = {noEndRadioButton, countRadioButton, dateRadioButton};

        for (int i = 0 ; i < radioButtons.length ; i++) {
            final int I = i;
            radioButtons[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(RadioButton radioButton : radioButtons) {radioButton.setChecked(false);}
                    radioButtons[I].setChecked(true);
                }
            });
        }


        countEditText = dialog.findViewById(R.id.edit_text_repeat_count);
        countEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999", countEditText)});
        int count = (Objects.nonNull(repeat.getNotifyRemainCount()))? repeat.getNotifyRemainCount() : 1; // repeatに設定がない場合、初期値を1とする
        countEditText.setText(String.valueOf(count), TextView.BufferType.NORMAL);

        final TextView dateTextView = dialog.findViewById(R.id.text_view_end_date);
        now.add(Calendar.MONTH, 1);
        dateTextView.setText(format.format(now.getTime()));
        now.add(Calendar.MONTH, -1);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    // TODO:終了日の日時は設定すべき？
                                    notifyEndDate.set(year, month, dayOfMonth);
                                    dateTextView.setText(format.format(notifyEndDate.getTime()));
                                }
                            }, year, month + 1, dayOfMonth);
                    datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
                } else {
                    datePickerDialog.updateDate(notifyEndDate.get(Calendar.YEAR), notifyEndDate.get(Calendar.MONTH), notifyEndDate.get(Calendar.DAY_OF_MONTH));
                }
                datePickerDialog.show();
            }
        });

        Button buttonSave = dialog.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSaveClicked(repeatId);
            }
        });
        return dialog;
    }

    public interface RepeatDialogFragmentListener {
        void onSaveClicked(long repeatId);
    }

    public void setRepeatDialogFragmentListener(RepeatDialogFragmentListener listener) {
        this.listener = listener;
    }

    public void removeRepeatDialogFragmentListener() {
        this.listener = null;
    }


    // 通知間隔のSpinnerを設定された値によって更新する
    public void updateIntervalSpinner() {

        // Adapterの変更で選択された場所が変わらないように、現在のポジションを再設定する
        int position = scaleSpinner.getSelectedItemPosition();

        if (Integer.parseInt(intervalEditText.getText().toString()) == 1 || Objects.isNull(intervalEditText.getText())) {
            scaleSpinner.setAdapter(everyAdapter);
        } else {
            scaleSpinner.setAdapter(intervalAdapter);
        }

        scaleSpinner.setSelection(position);
    }

//    public void updateSummry() {
//
//        // ※リピート間隔の表示
//        // ｛毎（頻度）/（間隔）（頻度）ごとに｝
//        // ｛○曜日　/　同じ日　/　第N○曜日　/　末日｝
//        // にリピート
//        // （｛残りN回　/　YYYY/MM/DDまで｝）
//
//        TextView summryTextView;
//        String summryFrequency;
//        String summryEnd;
//
//        for () {
//            // 設定された通知間隔を元にsummryFrequencyに設定
//            switch (scaleSpinner.getSelectedItemPosition) {
//                case 0: // 通知間隔が日単位
//                    (intervalEditText.getInt
//                    summryFrequency = 日　ごとに
//                    break;
//                case 1: // 通知間隔が週単位
//                    break;
//                case 2: // 通知間隔が月単位
//                    break;
//                case 3: // 通知間隔が年単位
//                    break;
//            }
//        }
//    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;
        private EditText editText;

        public InputFilterMinMax(int min, int max, EditText editText) {
            this.min = min;
            this.max = max;
            this.editText = editText;
        }

        public InputFilterMinMax(String min, String max, EditText editText) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
            this.editText = editText;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(source.toString());
                if (isInRange(min, max, input)) { // 最大値と最小値の間に収まる場合
                    return null;
                } else if (input < min || Objects.isNull(input)) { // 最小値以下、または、空欄（null）の場合、強制的に最小値を表示
                    editText.setText(String.valueOf(min));
                } else if (max < input) { // 最大値以上の場合、強制的に最大値を表示
                    editText.setText(String.valueOf(max));
                }
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        // minとmaxが逆だった場合に入れ替える処理
        private boolean isInRange(int min, int max, int input) {
            return max > min ? input >= min && input <= max : input >= max && input <= min;
        }
    }

}
