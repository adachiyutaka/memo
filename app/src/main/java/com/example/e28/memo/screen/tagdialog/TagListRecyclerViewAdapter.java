package com.example.e28.memo.screen.tagdialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import com.example.e28.memo.model.Tag;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.RealmResults;
import io.realm.RealmResults;

/**
 * Created by User on 2019/07/16.
 */

/**
 * Created by User on 2019/07/16.
 */
public class TagListRecyclerViewAdapter extends RecyclerView.Adapter<TagListRecyclerViewAdapter.TagCardViewHolder> {
    public RealmResults<Tag> tagRealmResults;

    // ViewHolderクラスの設定（インナークラス）
    public class TagCardViewHolder extends RecyclerView.ViewHolder {
        public CheckBox tagChk;

        public TagCardViewHolder(View itemView) {
            super(itemView);
            tagChk = itemView.findViewById(R.id.checkbox_tag);
        }
    }

    public TagListRecyclerViewAdapter(RealmResults<Tag> tagRealmResults) {
        this.tagRealmResults = tagRealmResults;
    }

    @Override
    public TagCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tag, parent,false);
        final TagCardViewHolder viewHolder = new TagCardViewHolder(view);
        // ViewHolderにクリックイベントを登録
        viewHolder.tagChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                onItemClick(viewHolder.tagChk, position, tagRealmResults.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagCardViewHolder viewHolder, int position) {
        Tag tag = tagRealmResults.get(position);
        // ViewHolderにTag.Nameをセット
        viewHolder.tagChk.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tagRealmResults.size();
    }

    void onItemClick(CheckBox tagChk, int position, Tag tag) {
        // アダプタのインスタンス側からこのメソッドをオーバーライドして
        // クリックイベントの処理を設定する
    }
}