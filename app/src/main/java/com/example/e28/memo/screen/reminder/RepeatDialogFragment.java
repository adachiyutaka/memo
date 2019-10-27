package com.example.e28.memo.screen.reminder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/10/25.
 */

public class RepeatDialogFragment extends DialogFragment {

    Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        // フルスクリーンでレイアウトを表示する
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_fragment_reminder);





        return dialog;
    }
}
