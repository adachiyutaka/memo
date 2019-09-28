package com.example.e28.memo.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/14.
 */

public class Memo extends RealmObject {
    @PrimaryKey
    public long id;
    public Date createdAt;
    public Date updatedAt;
    public String text;
    public String imagePath;
    public boolean isTagged;
    public RealmList<Tag> tagList;
    public boolean isTodo;
    public RealmList<Todo> toDoList;
    public boolean isHighlight;
    public boolean isChecked;
    public boolean isTrash;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    
    public boolean getIsTagged() {
        return isTagged;
    }

    public void setIsTagged(boolean isTagged) {
        this.isTagged = isTagged;
    }

    public RealmList<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(RealmList<Tag> tagList) {
        this.tagList = tagList;
    }

}
