package com.example.e28.memo;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/14.
 */


public class Todo extends RealmObject {
    @PrimaryKey
    public long id;
    public Date createdAt;
    public Date updatedAt;
    public long notifyStartTime;
    public boolean isRepeat;
    public RealmList<Repeat> RepeatList;
}