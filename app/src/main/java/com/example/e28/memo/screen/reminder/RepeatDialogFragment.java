package com.example.e28.memo.screen.reminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Repeat;
import com.example.e28.memo.model.Todo;
import com.google.android.flexbox.FlexboxLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;

import static com.example.e28.memo.screen.reminder.ReminderDialogFragment.IS_REPEAT;
import static com.example.e28.memo.screen.reminder.ReminderDialogFragment.REPEAT_ID;

/**
 * Created by User on 2019/10/25.
 */

public class RepeatDialogFragment extends DialogFragment {

    Realm realm;
    Context context;
    Dialog dialog;
    RepeatDialogFragmentListener listener;
    long repeatId;
    Repeat repeat;
    Boolean isNewRepeat;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    EditText intervalEditText;
    Spinner scaleSpinner;
    TextView everyDayTextView;
    ArrayAdapter<CharSequence> intervalAdapter;
    Calendar now;
    String week[];
    CheckBox dOWCheckBoxes[];
    RadioGroup sameDayRadioGroup;
    RadioButton sameDayRadioButton;
    RadioButton sameDOWRadioButton;
    RadioButton sameLastDayRadioButton;
    DatePickerDialog datePickerDialog;
    EditText countEditText;
    TextView countTextView;
    TextView dateTextView;
    RadioButton noEndRadioButton;
    RadioButton countRadioButton;
    RadioButton dateRadioButton;
    RadioButton[] radioButtons;
    TextView summaryTextView;

    boolean initialView;
    String TAG = "mytext";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

        // Realmのインスタンス作成
        realm = Realm.getDefaultInstance();
        initialView = true;

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
        dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_repeat);


        // リピートの初期値を設定
        repeatId = getArguments().getLong(REPEAT_ID);
        isNewRepeat = !getArguments().getBoolean(IS_REPEAT);
        if (isNewRepeat) {
            //登録のない場合は新規作成
            repeat = new Repeat();
            repeat.setRepeatScale(0);
            repeat.setNoEnd(true);
            repeat.setRepeatInterval(1);
        } else {
            //既存のrepeatIdだった場合はRealmから読み込み
            repeat = realm.where(Repeat.class).equalTo("id", repeatId).findFirst();
        }
        // 通知間隔の値を受け取るEditTextを作成
        intervalEditText = dialog.findViewById(R.id.edit_text_interval);
        // 既存のリピート情報を読み取り
        intervalEditText.setText(String.valueOf(repeat.getRepeatInterval()), TextView.BufferType.NORMAL);
        // 入力制限（1~99）を設定する
        intervalEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "99", intervalEditText)});
        // 通知間隔が「1」だった場合に表記を更新するリスナーをセット
        intervalEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInterval();
                updateSummary();
            }
        });


        // 通知間隔の横に「毎日」などを表示するTextViewを作成
        everyDayTextView = dialog.findViewById(R.id.text_view_every_day);


        // 通知間隔が「週ごと」の場合に表示する曜日チェックボックスを作成
        final FlexboxLayout dayOfWeekFragment = dialog.findViewById(R.id.fragment_day_of_week);
        dOWCheckBoxes = new CheckBox[7];
        String[] dayOfWeekStrings = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        // 曜日チェックボックスにリスナーと初期値をセット
        for (int i = 0 ; i < dayOfWeekStrings.length ; i++) {
            final CheckBox dOWCheckBox = dialog.findViewById(getResources().getIdentifier("check_box_" + dayOfWeekStrings[i], "id", context.getPackageName()));
            dOWCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSummary();
                }
            });
            if (repeat.getRepeatInterval() == 2) {
                // 既存の情報をセット
                switch (i) {
                    case 0:
                        dOWCheckBox.setChecked(repeat.isNotifySunday());
                        break;
                    case 1:
                        dOWCheckBox.setChecked(repeat.isNotifyMonday());
                        break;
                    case 2:
                        dOWCheckBox.setChecked(repeat.isNotifyTuesday());
                        break;
                    case 3:
                        dOWCheckBox.setChecked(repeat.isNotifyWednesday());
                        break;
                    case 4:
                        dOWCheckBox.setChecked(repeat.isNotifyThursday());
                        break;
                    case 5:
                        dOWCheckBox.setChecked(repeat.isNotifyFriday());
                        break;
                    case 6:
                        dOWCheckBox.setChecked(repeat.isNotifySaturday());
                        break;
                }
            } else {
                // 既存の情報がない場合、現在の曜日をセット
                if(now.get(Calendar.DAY_OF_WEEK) - 1 == i) dOWCheckBox.setChecked(true);
                // 月ごと、週ごとのボタン郡は、初期状態ではすべて非表示
                dayOfWeekFragment.setVisibility(View.GONE);
            }
            dOWCheckBoxes[i] = dOWCheckBox;
        }


        // 通知間隔が「月ごと」の場合に表示する「同じ日」、「第1金曜日」などのラジオボタンを作成
        sameDayRadioButton = dialog.findViewById(R.id.radio_button_same_day);
        sameDOWRadioButton = dialog.findViewById(R.id.radio_button_same_dow);
        sameLastDayRadioButton = dialog.findViewById(R.id.radio_button_same_last_day);
        // ラジオグループ変更時にサマリーを更新する
        sameDayRadioGroup = dialog.findViewById(R.id.radio_group_same_day);
        sameDayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (initialView) {
                    initialView = false;
                } else {
                    updateSummary();
                }
            }
        });

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
            sameLastDayRadioButton.setVisibility(View.GONE);
        }


        //　通知間隔を受け取るスピナーを作成
        scaleSpinner = dialog.findViewById(R.id.spinner_scale);
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
                        sameLastDayRadioButton.setVisibility(View.GONE);
                        break;
                    case 1:
                        dayOfWeekFragment.setVisibility(View.VISIBLE);
                        sameDayRadioButton.setVisibility(View.GONE);
                        sameDOWRadioButton.setVisibility(View.GONE);
                        sameLastDayRadioButton.setVisibility(View.GONE);
                        break;
                    case 2:
                        dayOfWeekFragment.setVisibility(View.GONE);
                        sameDayRadioButton.setVisibility(View.VISIBLE);
                        sameDOWRadioButton.setVisibility(View.VISIBLE);
                        if (dayOfMonth == lastDayOfMonth) {
                            sameLastDayRadioButton.setVisibility(View.VISIBLE);
                        } else {
                            sameLastDayRadioButton.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        dayOfWeekFragment.setVisibility(View.GONE);
                        sameDayRadioButton.setVisibility(View.GONE);
                        sameDOWRadioButton.setVisibility(View.GONE);
                        sameLastDayRadioButton.setVisibility(View.GONE);
                        break;
                }
                // 通知間隔のスピナーの横の「毎日」などの表示を更新
                updateInterval();
                // サマリーを更新する
                updateSummary();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 既存のデータを読み込んで設定
        scaleSpinner.setSelection(repeat.getRepeatScale());


        // 通知終了日の設定ラジオボタンを作成
        noEndRadioButton = dialog.findViewById(R.id.radio_button_no_end);
        countRadioButton = dialog.findViewById(R.id.radio_button_repeat_count);
        dateRadioButton = dialog.findViewById(R.id.radio_button_end_date);
        radioButtons = new RadioButton[]{noEndRadioButton, countRadioButton, dateRadioButton};
        // ラジオグループのように動かすためのリスナーを設定
        for (int i = 0 ; i < radioButtons.length ; i++) {
            final int I = i;
            // ラジオボタンの文字色を、チェック / 非チェックで切り替えるリスナーをセット
            radioButtons[I].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 一旦ラジオボタンに関わる全てのViewを非チェック / グレー表示にする
                    radioButtonsClear(radioButtons);
                    // クリックされたグループをチェック状態にする
                    radioButtons[I].setChecked(true);
                    radioButtons[I].setTextColor(Color.BLACK);
                    switch (I) {
                        case 0:
                            break;
                        case 1:
                            countEditText.setTextColor(Color.BLACK);
                            countTextView.setTextColor(Color.BLACK);
                            break;
                        case 2:
                            dateTextView.setTextColor(Color.BLACK);
                            break;
                    }
                    // サマリーを更新する
                    updateSummary();
                }
            });
        }


        countEditText = dialog.findViewById(R.id.edit_text_repeat_count);
        countTextView = dialog.findViewById(R.id.text_view_repeat_time);
        countEditText.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "999", countEditText)});
        countEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonsClear(radioButtons);
                countRadioButton.setChecked(true);
                countEditText.setTextColor(Color.BLACK);
                countTextView.setTextColor(Color.BLACK);
                countRadioButton.setTextColor(Color.BLACK);
                updateSummary();
            }
        });

        dateTextView = dialog.findViewById(R.id.text_view_end_date);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    notifyEndDate.set(year, month, dayOfMonth);
                                    dateTextView.setText(format.format(notifyEndDate.getTime()));
                                    radioButtonsClear(radioButtons);
                                    dateRadioButton.setChecked(true);
                                    dateTextView.setTextColor(Color.BLACK);
                                    dateRadioButton.setTextColor(Color.BLACK);
                                    updateSummary();
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


        // 保存ボタンの処理
        Button buttonSave = dialog.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat.setId(repeatId);
                if (isNewRepeat) {
                    repeat.setCreatedAt(now.getTime());
                } else {
                    repeat.setUpdatedAt(now.getTime());
                }
                repeat.setRepeatInterval(Integer.parseInt(intervalEditText.getText().toString()));
                int timeScale = scaleSpinner.getSelectedItemPosition();
                repeat.setRepeatScale(timeScale + 1);
                if(timeScale == 1) {
                    for (int i = 0; i < dOWCheckBoxes.length; i++) {
                        switch (i) {
                            case 0:
                                repeat.setNotifySunday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 1:
                                repeat.setNotifyMonday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 2:
                                repeat.setNotifyTuesday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 3:
                                repeat.setNotifyWednesday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 4:
                                repeat.setNotifyThursday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 5:
                                repeat.setNotifyFriday(dOWCheckBoxes[i].isChecked());
                                break;
                            case 6:
                                repeat.setNotifySaturday(dOWCheckBoxes[i].isChecked());
                                break;
                        }
                    }
                } else if (timeScale == 2) {
                    switch (sameDayRadioGroup.getCheckedRadioButtonId()) {
                        case R.id.radio_button_same_day:
                            repeat.setNotifySameDay(true);
                            break;
                        case R.id.radio_button_same_dow:
                            repeat.setNotifySameDOW(now.get(Calendar.DAY_OF_WEEK));
                            repeat.setNotifySameDOWOrdinal(now.get(Calendar.DAY_OF_WEEK_IN_MONTH));
                            break;
                        case R.id.radio_button_same_last_day:
                            repeat.setNotifySameLastDay(true);
                            break;
                    }
                }
                int checkedRadioButtonId = 0;
                for (RadioButton radioButton : radioButtons) {
                    checkedRadioButtonId = (radioButton.isChecked())? radioButton.getId() : null;
                }
                switch (checkedRadioButtonId) {
                    case R.id.radio_button_no_end:
                        repeat.setNoEnd(true);
                        break;
                    case R.id.radio_button_repeat_count:
                        repeat.setEndCount(true);
                        repeat.setNotifyEndCount(Integer.parseInt(countEditText.getText().toString()));
                        break;
                    case R.id.radio_button_end_date:
                        repeat.setEndDate(true);
                        repeat.setNotifyEndDate(notifyEndDate.getTime());
                        break;
                }

                repeat.setSummary(summaryTextView.getText().toString());

                // Realmにrepeatを保存
                saveRealmRepeat(repeat);
                listener.onSaveClicked(repeatId);
            }
        });

        // サマリーを更新する
        updateSummary();

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


    // 通知間隔が1の場合「毎日」などを表示する
    public void updateInterval() {

        // 通知間隔のEditTextから現在の間隔を判定する
        if (Integer.parseInt(intervalEditText.getText().toString()) == 1 || Objects.isNull(intervalEditText.getText())) {
            // 通知間隔のSpinnerから現在の値を判定する
            switch(scaleSpinner.getSelectedItemPosition()) {
                case 0:
                    everyDayTextView.setText("（毎日）");
                    break;
                case 1:
                    everyDayTextView.setText("（毎週）");
                    break;
                case 2:
                    everyDayTextView.setText("（毎月）");
                    break;
                case 3:
                    everyDayTextView.setText("（毎年）");
                    break;
            }
            everyDayTextView.setVisibility(View.VISIBLE);
        } else {
            everyDayTextView.setVisibility(View.GONE);
        }
    }

    public void updateSummary() {

        summaryTextView = dialog.findViewById(R.id.text_view_repeat_summary);
        String summaryFrequency;
        String summaryEnd = "";
        String extraTimeScaleString = null;

        int timeInterval = Integer.valueOf(intervalEditText.getText().toString());
        int timeScale = scaleSpinner.getSelectedItemPosition();
        Log.d(TAG, "updateSummary: timeScale init" + timeScale);
        String timeScaleString[] = {"日", "週", "月", "年"};

        switch (timeScale) {
            case 0: // 通知間隔が「日」の場合は、その他に付属する情報なし
                break;
            case 1: // 通知間隔が「週」の場合は、水曜日、金曜日などの指定を取得する
                extraTimeScaleString = "";
                for (int i = 0; i < dOWCheckBoxes.length; i++) {
                    if (i == 0) {
                        extraTimeScaleString = (dOWCheckBoxes[i].isChecked())? week[i] + "曜日" : "";
                    } else {
                        extraTimeScaleString += (dOWCheckBoxes[i].isChecked())?  "、" + week[i] + "曜日" : "";
                    }
                }
                break;
            case 2: // 通知間隔が「月」の場合は、同日、第3木曜日などを取得する
                extraTimeScaleString = "";
                RadioButton monthRadioButtons[] = {sameDayRadioButton, sameDOWRadioButton, sameLastDayRadioButton};
                for (int i = 0; i < monthRadioButtons.length; i++) {
                    if (monthRadioButtons[i].isChecked()) {
                        extraTimeScaleString = monthRadioButtons[i].getText().toString();
                    } else {}
                }
                break;
            case 3: // 通知間隔が「年」の場合は、その他に付属する情報なし
                break;
        }

        if (Objects.nonNull(extraTimeScaleString)){
            extraTimeScaleString += "に";
        } else {
            extraTimeScaleString = "";
        }

        if (timeInterval == 1) {
            Log.d(TAG, "updateSummary: timeScale" + timeScale);
            summaryFrequency = "毎" + timeScaleString[timeScale] + extraTimeScaleString;
        } else {
            summaryFrequency = String.valueOf(timeInterval) + timeScaleString[timeScale] + "ごとに" + extraTimeScaleString;
        }

        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                switch (i) {
                    case 0:
                        summaryEnd = "";
                        break;
                    case 1:
                        summaryEnd = "（" + countEditText.getText().toString() + "回）";
                        break;
                    case 2:
                        summaryEnd = "（" + dateTextView.getText().toString() + "まで）";
                        break;
                }
            }
        }

        summaryTextView.setText(summaryFrequency + "くり返し" +  summaryEnd);
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
                String destString = dest.toString();
                // 入力済みの文字 + 入力中の文字 をintに変換　（ex: 10 の1と0の間に 5 を入力する場合、↓は 1 + 5 + 0 = 150 となる）
                int input = Integer.parseInt(destString.substring(0, dstart) + source.toString() + destString.substring(dstart));
                if (isInRange(min, max, input)) { // 最大値と最小値の間に収まる場合
                    return null;
                } else if (input < min) { // 最小値以下、または、空欄（null）の場合、強制的に最小値を表示
                    editText.setText(String.valueOf(min));
                } else if (max < input) { // 最大値以上の場合、強制的に最大値を表示
                    editText.setText(String.valueOf(max));
                }
                return null;
            } catch (NumberFormatException nfe) {
            }
            return null;
        }

        // minとmaxが逆だった場合に入れ替える処理
        private boolean isInRange(int min, int max, int input) {
            return max > min ? input >= min && input <= max : input >= max && input <= min;
        }

    }

    public void radioButtonsClear(RadioButton[] radioButtons) {
        // 一旦全てのRadioButtonに関連するViewを非チェック状態にする
        for(int i = 0 ; i < radioButtons.length ; i++) {
            radioButtons[i].setChecked(false);
            radioButtons[i].setTextColor(Color.GRAY);
        }

        countEditText.setTextColor(Color.GRAY);
        countTextView.setTextColor(Color.GRAY);
        dateTextView.setTextColor(Color.GRAY);
    }


    // RealmにRepeatを保存する
    public void saveRealmRepeat(final Repeat repeat){
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(repeat);
                }
            });

            // recyclerViewの更新を求めるintentを送る
            Intent intent = new Intent("LIST_UPDATE");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } finally {
            Log.d("realm","saveRepeat:success");
        }
    }
}
