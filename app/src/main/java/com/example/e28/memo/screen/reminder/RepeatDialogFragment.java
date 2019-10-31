package com.example.e28.memo.screen.reminder;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/10/25.
 */

public class RepeatDialogFragment extends DialogFragment {

    Context context;
    RepeatDialogFragmentListener listener;
    long repeatId;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v4.app.Fragment dayOfWeekFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        dayOfWeekFragment = new DayOfWeekFragment();

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(R.layout.dialog_fragment_repeat);

        EditText intervalEditText = dialog.findViewById(R.id.text_view_interval);

        Spinner scaleSpinner = dialog.findViewById(R.id.spinner_scale);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.time_scale, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scaleSpinner.setAdapter(adapter);
        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fragmentTransaction.hide(dayOfWeekFragment);
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.day_of_week_choice_fragment, dayOfWeekFragment);
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction.hide(dayOfWeekFragment);
                        break;
                    case 3:
                        fragmentTransaction.hide(dayOfWeekFragment);
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

        TextView noRepeatTextView = dialog.findViewById(R.id.text_view_no_repeat);
        EditText countEditText = dialog.findViewById(R.id.edit_text_repeat_count);
        EditText dateEditText = dialog.findViewById(R.id.edit_text_end_date);

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
