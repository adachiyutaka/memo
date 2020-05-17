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
    public RealmList<Long> tagIdList;
    public boolean isTodo;
    public RealmList<Long> todoIdList;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void setTagged(boolean tagged) {
        isTagged = tagged;
    }

    public RealmList<Long> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(RealmList<Long> tagIdList) {
        this.tagIdList = tagIdList;
    }

    public boolean isTodo() {
        return isTodo;
    }

    public void setTodo(boolean todo) {
        isTodo = todo;
    }

    public RealmList<Long> getTodoIdList() {
        return todoIdList;
    }

    public void setTodoIdList(RealmList<Long> todoIdList) {
        this.todoIdList = todoIdList;
    }

    public boolean isHighlight() {
        return isHighlight;
    }

    public void setHighlight(boolean highlight) {
        isHighlight = highlight;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isTrash() {
        return isTrash;
    }

    public void setTrash(boolean trash) {
        isTrash = trash;
    }
}
