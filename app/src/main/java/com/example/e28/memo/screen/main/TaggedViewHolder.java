package com.example.e28.memo.screen.main;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.e28.memo.R;

/**
 * Created by User on 2019/07/16.
 */


public class TaggedViewHolder extends RecyclerView.ViewHolder {
    public TextView tagNameView;
    public TextView tagSummaryView;
    public TaggedViewHolder(View itemView) {
        super(itemView);
        tagNameView = (TextView) itemView.findViewById(R.id.text_name_tag);
        tagSummaryView = (TextView) itemView.findViewById(R.id.text_tag_summary);
    }
}