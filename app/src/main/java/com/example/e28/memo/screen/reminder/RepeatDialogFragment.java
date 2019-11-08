package com.example.e28.memo.screen.reminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
    CheckBox checkBoxes[];
    RadioButton sameDayRadioButton;
    RadioButton sameDOWRadioButton;
    RadioButton sameLastRadioButton;
    DatePickerDialog datePickerDialog;
    EditText countEditText;
    TextView dateTextView;

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

        Boolean isNewRepeat = true;

        // 通知間隔の値を受け取るEditTextを作成
        intervalEditText = dialog.findViewById(R.id.text_view_interval);
        // 既存のリピート情報を読み取り（設定がない場合、初期値を1とする）
        int interval = (isNewRepeat)? 1 : repeat.getRepeatInterval();
        intervalEditText.setText(String.valueOf(interval), TextView.BufferType.NORMAL);
        // 入力制限（1~99）を設定する
        intervalEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99", intervalEditText)});
        // 通知間隔が「1」だった場合に表記を更新するリスナーをセット
        intervalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIntervalSpinner();
            }
        });


        // 通知間隔が「週ごと」の場合に表示する曜日チェックボックスを作成
        final FlexboxLayout dayOfWeekFragment = dialog.findViewById(R.id.fragment_day_of_week);
        checkBoxes = new CheckBox[7];
        String[] dayOfWeekStrings = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        // 曜日ボタンの既存のリピート情報を読み取り
        boolean[] checkedWeek = new boolean[7];
        if (repeat.getRepeatScale() == 2) {
            if (repeat.isNotifyMonday()) checkedWeek[0] = true;
            if (repeat.isNotifyMonday()) checkedWeek[1] = true;
            if (repeat.isNotifyMonday()) checkedWeek[2] = true;
            if (repeat.isNotifyMonday()) checkedWeek[3] = true;
            if (repeat.isNotifyMonday()) checkedWeek[4] = true;
            if (repeat.isNotifyMonday()) checkedWeek[5] = true;
            if (repeat.isNotifyMonday()) checkedWeek[6] = true;
        }
        // 曜日チェックボックスにリスナーと初期値をセット
        for (int i = 0 ; i < dayOfWeekStrings.length ; i++) {
            final CheckBox dOWCheckBox = dialog.findViewById(getResources().getIdentifier("check_box_" + dayOfWeekStrings[i], "id", context.getPackageName()));
            if (repeat.isNotifySameDOW()) {
                // 既存の情報をセット
                if(checkedWeek[i]) dOWCheckBox.setChecked(true);
            } else {
                // 既存の情報がない場合、現在の曜日をセット
                if(now.get(Calendar.DAY_OF_WEEK) == i) dOWCheckBox.setChecked(true);
                // 月ごと、週ごとのボタン郡は、初期状態ではすべて非表示
                dayOfWeekFragment.setVisibility(View.GONE);
            }
        }


        // 通知間隔が「月ごと」の場合に表示する「同じ日」、「第1金曜日」などのラジオボタンを作成
        final RadioButton sameDayRadioButton = dialog.findViewById(R.id.radio_button_same_day);
        final RadioButton sameDOWRadioButton = dialog.findViewById(R.id.radio_button_same_dow);
        final RadioButton sameLastDayRadioButton = dialog.findViewById(R.id.radio_button_same_last_day);
        // 月ごとのラジオボタンに初期値を設定
        if (repeat.getRepeatInterval() == 3) {
            // 既存の情報をセット
            if (repeat.isNotifySameDay) sameDayRadioButton.setChecked(true);
            if (repeat.isNotifySameDOW) {
                sameDOWRadioButton.setChecked(true);
                sameDOWRadioButton.setText("その月の第" + String.valueOf(repeat.getNotifySameDOWOrdinal())+ week[repeat.getNotifySameDOW()] + "曜日");
            }
            if (repeat.isNotifySameLastDay) sameLastDayRadioButton.setChecked(true);
        } else {
            // 既存の情報がない場合、「その月の同じ日」をセット
            sameDayRadioButton.setChecked(true);
            // 「第1金曜日」などのラジオボタンに現在の値をセット
            sameDOWRadioButton.setText("その月の第" + now.get(Calendar.DAY_OF_WEEK_IN_MONTH) + week[now.get(Calendar.DAY_OF_WEEK)] + "曜日");
            // 月ごと、週ごとのボタン郡は、初期状態ではすべて非表示
            sameDayRadioButton.setVisibility(View.GONE);
            sameDOWRadioButton.setVisibility(View.GONE);
            sameLastRadioButton.setVisibility(View.GONE);
        }


        //　通知間隔を受け取るスピナーを作成
        scaleSpinner = dialog.findViewById(R.id.spinner_scale);
        everyAdapter = ArrayAdapter.createFromResource(context, R.array.time_scale_every, android.R.layout.simple_spinner_item);
        everyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalAdapter = ArrayAdapter.createFromResource(context, R.array.time_scale, android.R.layout.simple_spinner_item);
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // スピナーにリスナーを設定
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
        // 既存のデータを読み込んで設定
        int intervalPosition = (isNewRepeat)? 0 : repeat.getRepeatInterval();
        scaleSpinner.setSelection(intervalPosition);
        updateIntervalSpinner();


        // 通知終了日の設定ラジオボタンを作成
        RadioButton noEndRadioButton = dialog.findViewById(R.id.radio_button_no_end);
        RadioButton countRadioButton = dialog.findViewById(R.id.radio_button_repeat_count);
        RadioButton dateRadioButton = dialog.findViewById(R.id.radio_button_end_date);
        final RadioButton[] radioButtons = {noEndRadioButton, countRadioButton, dateRadioButton};
        LinearLayout noEndGroup = dialog.findViewById(R.id.Group_no_end);
        LinearLayout countGroup = dialog.findViewById(R.id.Group_count);
        LinearLayout dateGroup = dialog.findViewById(R.id.Group_date);
        final LinearLayout[] radioGroups = {noEndGroup, countGroup, dateGroup};

        // ラジオグループのように動かすためのリスナーを設定
        for (int i = 0 ; i < radioGroups.length ; i++) {
            final int I = i;
            // ラジオボタンの文字色を、チェック / 非チェックで切り替えるリスナーをセット
            radioGroups[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 一旦全てのViewを非チェック状態にする
                    for(int j = 0 ; j < radioButtons.length ; j++) {
                        RadioButton radioButton = (RadioButton)radioButtons[j];
                        radioButton.setChecked(false);
                        radioButton.setTextColor(Color.GRAY);
                    }
                    countEditText.setTextColor(Color.GRAY);
                    dateTextView.setTextColor(Color.GRAY);
                    // クリックされたグループをチェック状態にする
                    RadioButton radioButton = (RadioButton)radioButtons[I];
                    radioButton.setChecked(true);
                    radioButton.setTextColor(Color.BLACK);
                    switch (I) {
                        case 0:
                            break;
                        case 1:
                            countEditText.setTextColor(Color.BLACK);
                            break;
                        case 2:
                            dateTextView.setTextColor(Color.BLACK);
                            break;
                    }
                }
            });
        }

        countEditText = dialog.findViewById(R.id.edit_text_repeat_count);
        countEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999", countEditText)});

        dateTextView = dialog.findViewById(R.id.text_view_end_date);
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


        // ラジオグループに初期値を設定
        noEndRadioButton.performClick();
        if (repeat.isNoEnd) noEndRadioButton.performClick();
        if (repeat.isEndCount) {
            countRadioButton.performClick();
            countEditText.setText(repeat.getNotifyRemainCount());
        } else {
            countEditText.setText("10");
        }
        if (repeat.isEndDate) {
            dateRadioButton.performClick();
            dateTextView.setText(format.format(repeat.getNotifyEndDate()));
        } else {
            now.add(Calendar.MONTH, 1);
            dateTextView.setText(format.format(now.getTime()));
            now.add(Calendar.MONTH, -1);
        }

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

    public void updateSummry() {

        TextView summryTextView;
        String summryFrequency;
        String summryEnd;
        String extraTimeScaleString = null;

        int timeInterval = Integer.valueOf(intervalEditText.getText().toString());
        int timeScale = scaleSpinner.getSelectedItemPosition();
        String timeScaleString[] = {"日", "週", "月", "年"};

        switch (timeScale) {
            case 0: // 通知間隔が「日」の場合は、その他に付属する情報なし
                break;
            case 1: // 通知間隔が「週」の場合は、水曜日、金曜日などの指定を取得する
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (i == 0) {
                        extraTimeScaleString = (checkBoxes[i].isChecked())? week[i] + "曜日" : "";
                    } else {
                        extraTimeScaleString = (checkBoxes[i].isChecked())? extraTimeScaleString + "、" + week[i] + "曜日" : "";
                    }
                }
                break;
            case 2: // 通知間隔が「月」の場合は、同日、第3木曜日などを取得する
                RadioButton monthRadioButtons[] = {sameDayRadioButton, sameDOWRadioButton, sameLastRadioButton};
                for (int i = 0; i < monthRadioButtons.length; i++) {
                    extraTimeScaleString = (monthRadioButtons[i].isChecked())? monthRadioButtons[i].getText().toString() : "";
                }
                break;
            case 3: // 通知間隔が「年」の場合は、その他に付属する情報なし
                break;
        }

        extraTimeScaleString = (Objects.nonNull(extraTimeScaleString))? "、" + extraTimeScaleString : "";

        if (timeInterval == 1) {
            summryFrequency = "毎" + timeScaleString[timeScale] + extraTimeScaleString;
        } else {
            summryFrequency = String.valueOf(timeInterval) + timeScaleString[timeScale] + "ごとに" + extraTimeScaleString;
        }

    }



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
                } else if (input < min) { // 最小値以下、または、空欄（null）の場合、強制的に最小値を表示
                    editText.setText(String.valueOf(min));
                } else if (max < input) { // 最大値以上の場合、強制的に最大値を表示
                    editText.setText(String.valueOf(max));
                }
                return null;
            } catch (NumberFormatException nfe) { }
            editText.setText(String.valueOf(min));
            return null;
        }

        // minとmaxが逆だった場合に入れ替える処理
        private boolean isInRange(int min, int max, int input) {
            return max > min ? input >= min && input <= max : input >= max && input <= min;
        }
    }
}