package com.example.e28.memo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by User on 2019/07/16.
 */


public class TaggedViewHolder extends RecyclerView.ViewHolder {
    public TextView tagNameView;
    public TextView tagSummaryView;
    public TaggedViewHolder(View itemView) {
        super(itemView);
        tagNameView = (TextView) itemView.findViewById(R.id.tag_name);
        tagSummaryView = (TextView) itemView.findViewById(R.id.tag_summary);

    }
}