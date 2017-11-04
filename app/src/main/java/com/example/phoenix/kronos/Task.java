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
    String location;
    Date datetime;
    long id;

    public Task()
    {

    }
    public Task(LatLng latlng,String actionType,String location,Date datetime)
    {
        latlng=latlng; actionType=actionType; location=location; datetime=datetime;
    }
}
