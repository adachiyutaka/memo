package com.example.e28.memo.screen.memolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by User on 2019/11/24.
 */

public class NoteFragment extends Fragment {

    Realm realm;

    NoteRecyclerViewAdapter adapter;

    RecyclerView recyclerView;
    EditText editText;

    ArrayList<String> datasource = new ArrayList<>();

    int widthParentView;

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

        //ノートタイトルを表示するRecyclerView
        realm = Realm.getDefaultInstance();

        //旧レイアウト
        //RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
        //adapter = new TaggedRecyclerViewAdapter(memoRealmResults);

        RealmResults<Tag> tagRealmResults = realm.where(Tag.class).findAll();

        for (int i = 100; i < 110; i++ ) {
            datasource.add(String.valueOf(i));
        }

        recyclerView = view.findViewById(R.id.recycler_view_memo);
        editText = view.findViewById(R.id.edit_text_memo);
        editText = addTextChangedListener(this);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //TextViewに入力された値をリアルタイムで反映
            datasouce(0) = s;
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

        final LinearLayout nameCard = view.findViewById(R.id.linear_layout_name_card);

        ViewTreeObserver observer = nameCard.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        widthParentView = 1000;
                                nameCard.getWidth();
                        Log.d("MainActivity : ", "ボタン幅 = " + nameCard.getWidth());
                        Log.d("MainActivity : ", "ボタン高さ = " + nameCard.getHeight());
                    }
                });

        adapter = new NoteRecyclerViewAdapter(datasource, widthParentView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                if(e.getAction() == MotionEvent.ACTION_DOWN){
//                    memoEditText.setVisibility(View.GONE);
//                }else if(e.getAction() == MotionEvent.ACTION_UP){
//                    memoEditText.setVisibility(View.VISIBLE);
//                }
//                return false;
//            }

//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//            }
//        });
//


        ItemTouchHelper mHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//ここでドラッグ動作、Swipe動作を指定します
//ドラッグさせたくないとか、Swipeさせたくない場合はここで分岐してアクションを指定しないことでドラッグできない行などを指定できます
//ドラッグは長押しで自動的に開始されます
                return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT) | makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) |
                        makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }


            //ドラッグで場所を移動した際の処理を記述します
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                (recyclerView.getAdapter()).notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }


            //選択ステータスが変更された場合の処理を指定します
//この例ではAdapterView内のcontainerViewを表示にしています
//containerViewには背景色を指定しており、ドラッグが開始された際に見やすくなるようにしています
//            @Override
//            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                super.onSelectedChanged(viewHolder, actionState);
//
//                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
//                    ((adapter.holder) viewHolder).container.setVisibility(View.VISIBLE);
//            }
            //選択が終わった時（Dragが終わった時など）の処理を指定します
//今回はアイテムをDropした際にcontainerViewを非表示にして通常表示に戻しています
//            @Override
//            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                super.clearView(recyclerView, viewHolder);
//                ((adapter.holder) viewHolder).container.setVisibility(View.GONE);
//            }


            //Swipeされた際の処理です。
//
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                int fromPos = viewHolder.getAdapterPosition();
                datasource.remove(fromPos);
                (recyclerView.getAdapter()).notifyItemRemoved(fromPos);
            }
        });

        mHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(mHelper);
        recyclerView.setAdapter(adapter);


//  ItemTouchHelper.SimpleCallbackを使った例（スワイプが動かない）
//        ItemTouchHelper itemDecor = new ItemTouchHelper(
//                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
//                        ItemTouchHelper.RIGHT) {
//
//
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                        final int fromPos = viewHolder.getAdapterPosition();
//                        final int toPos = target.getAdapterPosition();
//                        adapter.notifyItemMoved(fromPos, toPos);
//                        return true;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        final int fromPos = viewHolder.getAdapterPosition();
//                        datasource.remove(fromPos);
//                        adapter.notifyItemRemoved(fromPos);
//                        Log.d(TAG, "onSwiped: success");
//                    }
//                });
//        itemDecor.attachToRecyclerView(recyclerView);

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
