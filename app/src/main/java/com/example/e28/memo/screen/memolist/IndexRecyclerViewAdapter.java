package com.example.e28.memo.screen.memolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 2019/11/25.
 */

public class IndexRecyclerViewAdapter extends RecyclerView.Adapter<IndexRecyclerViewAdapter.ViewHolder>{
    public RealmResults<Memo> memoRealmResults;
    public RealmResults<Tag> tagRealmResults;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.text_view_tag_name);
        }
    }

    public IndexRecyclerViewAdapter(RealmResults<Tag> tagRealmResults) {
        this.tagRealmResults = tagRealmResults;
    }

    @Override
    public IndexRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_index, parent,false);
        IndexRecyclerViewAdapter.ViewHolder viewHolder = new IndexRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IndexRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Tag tag = tagRealmResults.get(position);
        String tagStr = "";

        // ViewHolderにMemo.textの文章をセット
        viewHolder.tagTextView.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tagRealmResults.size();
    }
}

