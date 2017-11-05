package com.example.phoenix.kronos;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by phoenix on 4/11/17.
 */


public class Task {
    LatLng latlng;
    String actionType;
    String description;
    String locationname;
    Date datetime;
    long id;

    public Task()
    {

    }
    public Task(LatLng latlng,String description,String actionType,String locationname,Date datetime)
    {
        this.latlng=latlng;
        this.actionType=actionType;
        this.locationname=locationname;
        this.datetime=datetime;
        this.description=description;
    }
}
