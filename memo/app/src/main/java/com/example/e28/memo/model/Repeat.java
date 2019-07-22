package com.example.e28.memo.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 2019/07/16.
 */

public class Repeat extends RealmObject {
    @PrimaryKey
    public long id;
    public Date createdAt;
    public Date updatedAt;
    public Integer repeatScale; //リピートなし=0、日=1、週=2、月=3、年=4
    public long repeatInterval;
    public boolean isNotifyMonday;
    public boolean isNotifyTuesday;
    public boolean isNotifyWednesday;
    public boolean isNotifyThursday;
    public boolean isNotifyFriday;
    public boolean isNotifySaturday;
    public boolean isNotifySunday;
    public long notifyRemainCount; //設定回数
    public long notifyEndCount; //残り回数
    public Date notifyEndDate;
}
