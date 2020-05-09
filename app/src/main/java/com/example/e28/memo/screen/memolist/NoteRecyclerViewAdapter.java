package com.example.e28.memo.screen.memolist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by User on 2019/11/25.
 */

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>{
    public RealmResults<Memo> memoRealmResults;
    public RealmResults<Tag> tagRealmResults;
    public ArrayList<String> datasource;
    public View.OnTouchListener listener;
    public int widthRecyclerView;

    public NoteRecyclerViewAdapter(ArrayList<String> datasource) {
        this.datasource = datasource;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    public NoteRecyclerViewAdapter(ArrayList<String> datasource, int widthRecyclerView) {
        //this.tagRealmResults = tagRealmResults;
        this.datasource = datasource;
        this.widthRecyclerView = widthRecyclerView;
    }

    public void setOnTouchListener(View.OnTouchListener listener){
        this.listener = listener;
    }

    @Override
    public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_memo_card, parent,false);
        //view.setScaleX((float)widthRecyclerView);
        NoteRecyclerViewAdapter.ViewHolder viewHolder = new NoteRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //Tag tag = tagRealmResults.get(position);
        //String tagStr = "";

        // ViewHolderにMemo.textの文章をセット
        viewHolder.textView.setText(datasource.get(position));
//        viewHolder.textView.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                listener.onTouch(v, event);
//                return true;
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }
}

