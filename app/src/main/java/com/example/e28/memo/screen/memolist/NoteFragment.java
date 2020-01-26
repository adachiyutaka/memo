package com.example.e28.memo.screen.memolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 2019/11/24.
 */

public class NoteFragment extends Fragment {

    Realm realm;

    NoteRecyclerViewAdapter adapter;

    // DB変更の有無を受信してrecyclerViewを更新するレシーバー
    private BroadcastReceiver listUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        final EditText memoEditText = view.findViewById(R.id.edit_text_memo);

        //ノートタイトルを表示するRecyclerView
        realm = Realm.getDefaultInstance();

        //旧レイアウト
        //RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
        //adapter = new TaggedRecyclerViewAdapter(memoRealmResults);

        RealmResults<Tag> tagRealmResults = realm.where(Tag.class).findAll();

        final ArrayList<String> datasource = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            datasource.add(String.valueOf(i));
        }

        adapter = new NoteRecyclerViewAdapter(datasource);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_memo);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    memoEditText.setVisibility(View.GONE);
                }else if(e.getAction() == MotionEvent.ACTION_UP){
                    memoEditText.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        ItemTouchHelper itemDecor = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {


                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        datasource.remove(fromPos);
                        adapter.notifyItemRemoved(fromPos);
                    }
                });
        //itemDecor.attachToRecyclerView(recyclerView);

        // RecyclerView更新用のレシーバーを作成
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listUpdateReceiver, new IntentFilter("LIST_UPDATE"));

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // realmのインスタンスを閉じる
        realm.close();
        // recyclerView更新用のレシーバーを破棄
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(listUpdateReceiver);
    }
}
