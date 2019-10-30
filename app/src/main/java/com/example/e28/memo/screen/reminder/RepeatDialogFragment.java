package com.example.e28.memo.screen.reminder;

import android.app.Dialog;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/10/25.
 */

public class RepeatDialogFragment extends DialogFragment {

    Context context;
    RepeatDialogFragmentListener listener;
    long repeatId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_repeat);

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
