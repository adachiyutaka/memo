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

import java.util.Calendar;

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
    Spinner scaleSpinner;
    Calendar now;
    DatePickerDialog datePickerDialog;

    String TAG = "mytext";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();



        now = Calendar.getInstance();
        final int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
        final int lastDayOfMonth = now.getActualMaximum(Calendar.DATE);

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_fragment_repeat);

        EditText intervalEditText = dialog.findViewById(R.id.text_view_interval);

        repeat = new Repeat();
        repeat.setNotifyEndDate(now.getTime());

        // 週ごと、月ごとにリピートする場合の詳細を指定するView郡
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
        final RadioButton sameDayRadioButton = dialog.findViewById(R.id.radio_button_same_day);
        final RadioButton sameDOWRadioButton = dialog.findViewById(R.id.radio_button_same_dow);
        final RadioButton sameLastRadioButton = dialog.findViewById(R.id.radio_button_same_last);
        // 最初はすべて非表示状態
        dayOfWeekFragment.setVisibility(View.GONE);
        sameDayRadioButton.setVisibility(View.GONE);
        sameDOWRadioButton.setVisibility(View.GONE);
        sameLastRadioButton.setVisibility(View.GONE);

        scaleSpinner = dialog.findViewById(R.id.spinner_scale);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.time_scale, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scaleSpinner.setAdapter(adapter);
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

        EditText countEditText = dialog.findViewById(R.id.edit_text_repeat_count);
        final TextView dateTextView = dialog.findViewById(R.id.text_view_end_date);
        dateTextView.setText(now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日");
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar notifyEndDateCalendar =  Calendar.getInstance();
                notifyEndDateCalendar.setTime(repeat.getNotifyEndDate());

                if (datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Calendar endDateCalendar = Calendar.getInstance();
                                    endDateCalendar.set(year, month, dayOfMonth);
                                    repeat.setNotifyEndDate(endDateCalendar.getTime());
                                    dateTextView.setText(endDateCalendar.get(Calendar.YEAR) + "年" + (endDateCalendar.get(Calendar.MONTH) + 1) + "月" + endDateCalendar.get(Calendar.DAY_OF_MONTH) + "日");
                                }
                            }, year, month + 2, dayOfMonth);
                    datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
                } else {
                    datePickerDialog.updateDate(notifyEndDateCalendar.get(Calendar.YEAR), notifyEndDateCalendar.get(Calendar.MONTH), notifyEndDateCalendar.get(Calendar.DAY_OF_MONTH));
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
}
