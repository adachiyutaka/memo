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
}
