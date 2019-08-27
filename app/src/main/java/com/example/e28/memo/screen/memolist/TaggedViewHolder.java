package com.example.e28.memo.screen.memolist;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.e28.memo.R;
/**
 * Created by User on 2019/07/16.
 */
public class TaggedViewHolder extends RecyclerView.ViewHolder {
    public TextView memoTextView;
    public TaggedViewHolder(View itemView) {
        super(itemView);
        memoTextView = (TextView) itemView.findViewById(R.id.text_memo);
    }
}

