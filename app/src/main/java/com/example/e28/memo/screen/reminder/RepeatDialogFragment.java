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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Repeat;

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
    DayOfWeekFragment dayOfWeekFragment;
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

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_fragment_repeat);

        EditText intervalEditText = dialog.findViewById(R.id.text_view_interval);

        repeat = new Repeat();
        repeat.setNotifyEndDate(now.getTime());

        scaleSpinner = dialog.findViewById(R.id.spinner_scale);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.time_scale, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scaleSpinner.setAdapter(adapter);
        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //fragmentTransaction.hide(dayOfWeekFragment);
                        break;
                    case 1:
//                        fragmentManager = getFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        dayOfWeekFragment = new DayOfWeekFragment();
//                        fragmentTransaction.replace(R.id.day_of_week_choice_fragment, dayOfWeekFragment);
//                        fragmentTransaction.commit();

                        break;
                    case 2:
                        //fragmentTransaction.hide(dayOfWeekFragment);
                        break;
                    case 3:
                        //fragmentTransaction.hide(dayOfWeekFragment);
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
