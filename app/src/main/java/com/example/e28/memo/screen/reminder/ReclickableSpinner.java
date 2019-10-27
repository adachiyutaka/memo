package com.example.e28.memo.screen.reminder;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by User on 2019/10/26.
 */

public class ReclickableSpinner extends android.support.v7.widget.AppCompatSpinner {

    public ReclickableSpinner(Context context)
    { super(context); }

    public ReclickableSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public ReclickableSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override public void
    setSelection(int position, boolean animate)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override public void
    setSelection(int position)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }
}
