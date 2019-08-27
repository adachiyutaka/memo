package com.example.e28.memo.screen.memolist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;

import io.realm.RealmResults;


/**
 * Created by User on 2019/07/16.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by User on 2019/07/16.
 */
public class TaggedRecyclerViewAdapter extends RecyclerView.Adapter<TaggedRecyclerViewAdapter.TaggedViewHolder> {
    public RealmResults<Memo> memoRealmResults;

    public class TaggedViewHolder extends RecyclerView.ViewHolder {
        public TextView memoTextView;
        public TaggedViewHolder(View itemView) {
            super(itemView);
            memoTextView = (TextView) itemView.findViewById(R.id.text_memo);
        }
    }


    public TaggedRecyclerViewAdapter(RealmResults<Memo> memoRealmResults) {
        this.memoRealmResults = memoRealmResults;
    }

    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tag, parent,false);
        TaggedViewHolder vh = new TaggedViewHolder(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaggedViewHolder viewHolder, int position) {
        viewHolder.memoTextView.setText(memoRealmResults.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return memoRealmResults.size();
    }
}

/*
public class TaggedRecyclerViewAdapter extends RecyclerView.Adapter<TaggedViewHolder> {
    public List<TagItem> list;
    public TaggedRecyclerViewAdapter(List<TagItem> list) {
        this.list = list;
    }
    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tag, parent,false);
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
*/