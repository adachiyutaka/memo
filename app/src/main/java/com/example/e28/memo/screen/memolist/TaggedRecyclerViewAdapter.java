package com.example.e28.memo.screen.memolist;

import android.view.View;
import android.view.ViewGroup;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;

import io.realm.RealmResults;


/**
 * Created by User on 2019/07/16.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 2019/07/16.
 */
public class TaggedRecyclerViewAdapter extends RecyclerView.Adapter<TaggedRecyclerViewAdapter.TaggedViewHolder> {
    public RealmResults<Memo> memoRealmResults;

    public class TaggedViewHolder extends RecyclerView.ViewHolder {
        public EditText memoEditText;
        public TextView memoDate;
        public TextView memoTag;

        public TaggedViewHolder(View itemView) {
            super(itemView);
            memoEditText = itemView.findViewById(R.id.text_memo);
            memoDate = itemView.findViewById(R.id.memo_card_date);
            memoTag = itemView.findViewById(R.id.memo_card_tag);
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
        Memo memo = memoRealmResults.get(position);

        // ViewHolderにMemo.textの文章をセット
        viewHolder.memoEditText.setText(memo.getText());

        // ViewHolderにMemo.createdAtの時刻をセット
        // createdAtの内容がNullの場合は何もセットしない
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd_HH:mm", Locale.JAPAN);
        try {
            viewHolder.memoDate.setText(sdf.format(memo.getCreatedAt()));
        } catch (NullPointerException e) {}

        // タグがある場合は、ViewHolderにMemo.tagの文章をセット
        boolean isTagged = memo.getIsTagged();
        if(isTagged){viewHolder.memoTag.setText("買い物");}
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