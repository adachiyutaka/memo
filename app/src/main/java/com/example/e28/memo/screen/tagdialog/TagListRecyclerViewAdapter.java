package com.example.e28.memo.screen.tagdialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.example.e28.memo.R;
import com.example.e28.memo.model.Tag;
import java.util.ArrayList;
import io.realm.RealmResults;

/**
 * Created by User on 2019/07/16.
 */

/**
 * Created by User on 2019/07/16.
 */
public class TagListRecyclerViewAdapter extends RecyclerView.Adapter<TagListRecyclerViewAdapter.TagCardViewHolder> {
    public RealmResults<Tag> tagRealmResults;
    ArrayList<Long> editedTagIdList;
    private OnTagCheckListener listener;

    // ViewHolderクラスの設定（インナークラス）
    public class TagCardViewHolder extends RecyclerView.ViewHolder {
        public CheckBox tagChk;

        public TagCardViewHolder(View itemView) {
            super(itemView);
            tagChk = itemView.findViewById(R.id.checkbox_tag);
        }
    }

    public TagListRecyclerViewAdapter(RealmResults<Tag> tagRealmResults, ArrayList<Long> tagIdList) {
        this.tagRealmResults = tagRealmResults;
        this.editedTagIdList = tagIdList;
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
                if (viewHolder.tagChk.isChecked() == true) {
                    // チェックが入った場合に、editedTagListに該当タグを追加
                    listener.onChangeCheck(true, position);
                } else {
                    // チェックが外れた場合に、editedTagListから該当タグを削除
                    listener.onChangeCheck(false, position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagCardViewHolder viewHolder, int position) {
        Tag tag = tagRealmResults.get(position);
        // ViewHolderにTag.Nameをセット
        viewHolder.tagChk.setText(tag.getName());
        // Memoの持つタグに該当する場合は、チェック済みにしておく
        if (editedTagIdList != null) {
            if (editedTagIdList.contains(tag.getId())){
            viewHolder.tagChk.setChecked(true);}
        }
    }

    @Override
    public int getItemCount() {
        return tagRealmResults.size();
    }

    public void setOnTagCheckListener(OnTagCheckListener listener) {
        this.listener = listener;
    }

    public void removeOnTagCheckListener() {
        this.listener = null;
    }

    public interface OnTagCheckListener {
        void onChangeCheck(boolean isChecked, int position);
    }
}