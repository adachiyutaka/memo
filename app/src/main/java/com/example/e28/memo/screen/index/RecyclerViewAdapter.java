package com.example.e28.memo.screen.index;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.constraint.Constraints.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    public View.OnTouchListener listener;
    public RealmResults<Tag> realmResults;

    public RecyclerViewAdapter(RealmResults<Tag> realmResults) {
        this.realmResults = realmResults;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewContent = itemView.findViewById(R.id.content);
        }
    }

    public void setOnTouchListener(View.OnTouchListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_index, parent,false);
        RecyclerViewAdapter.ViewHolder holder = new RecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Tag tag = realmResults.where().equalTo("id", position).findFirst();
        Log.d(TAG, "onBindViewHolder: position:" + position);
        holder.textViewTitle.setText(tag.getName());
        holder.textViewContent.setText("メモの内容");
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }
}
