package com.example.e28.memo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by User on 2019/07/16.
 */

public class TaggedRecyclerViewAdapter extends RecyclerView.Adapter<TaggedViewHolder> {

    private List<TagItem> list;

    public TaggedRecyclerViewAdapter(List<TagItem> list) {
        this.list = list;
    }

    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent,false);
        TaggedViewHolder vh = new TaggedViewHolder(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaggedViewHolder holder, int position) {
        holder.tagNameView.setText(list.get(position).getTagName());
        holder.tagSummaryView.setText(list.get(position).getTagSummary());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}