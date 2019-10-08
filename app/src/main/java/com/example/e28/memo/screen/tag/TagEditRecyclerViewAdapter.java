package com.example.e28.memo.screen.tag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;

import io.realm.RealmResults;

/**
 * Created by User on 2019/07/16.
 */

/**
 * Created by User on 2019/07/16.
 */
public class TagEditRecyclerViewAdapter extends RecyclerView.Adapter<TagEditRecyclerViewAdapter.TagCardViewHolder> {
    public RealmResults<Tag> tagRealmResults;

    // ViewHolderクラスの設定（インナークラス）
    public class TagCardViewHolder extends RecyclerView.ViewHolder {
        public EditText tagEditText;
        public ImageButton deleteBtn;
        public Button saveBtn;

        public TagCardViewHolder(View itemView) {
            super(itemView);
            tagEditText = itemView.findViewById(R.id.edit_text_tag);
            deleteBtn = itemView.findViewById(R.id.button_delete);
            saveBtn = itemView.findViewById(R.id.button_save);
        }
    }

    public TagEditRecyclerViewAdapter(RealmResults<Tag> tagRealmResults) {
        this.tagRealmResults = tagRealmResults;
    }

    @Override
    public TagCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tag_edit, parent,false);
        final TagCardViewHolder viewHolder = new TagCardViewHolder(view);
        // ViewHolderにクリックイベントを登録

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                onDeleteClick(tagRealmResults.get(position).getId());
            }
        });

        viewHolder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                //処理はonItemClick()に丸投げ
                onSaveClick(viewHolder.tagEditText, position, tagRealmResults.get(position).getId());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagCardViewHolder viewHolder, int position) {
        Tag tag = tagRealmResults.get(position);
        // ViewHolderにTag.Nameをセット
        viewHolder.tagEditText.setText(tag.getName());
    }

    @Override
    public int getItemCount() {
        return tagRealmResults.size();
    }

    void onDeleteClick(long id) {
        // アダプタのインスタンス側からこのメソッドをオーバーライドして
        // クリックイベントの処理を設定する
    }
    void onSaveClick(EditText editText, int position, long id) {
        // アダプタのインスタンス側からこのメソッドをオーバーライドして
        // クリックイベントの処理を設定する
    }
}