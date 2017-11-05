package com.example.phoenix.kronos;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject{
    double lat;
    double lon;
    @PrimaryKey
    int id;
    String actionType;
    String message;
    String nickname;

    public Task()
    {

    }
    public Task(double lat,double lon,int id,String actionType,String message,String nickname)
    {
        this.lat=lat; this.lon=lon;
        this.actionType=actionType; this.message=message;
        this.nickname=nickname;
    }
}
