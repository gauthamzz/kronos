package com.example.phoenix.kronos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.hypertrack.lib.HyperTrack;

import java.util.Date;

import io.realm.Realm;

public class FormActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST=1;
    double lat=0,lon=0;
    String actiontype="";
    Realm realm;
    EditText nickname,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm= Realm.getDefaultInstance();
        setContentView(R.layout.activity_form);
        nickname=(EditText)findViewById(R.id.nickname);
        message=(EditText)findViewById(R.id.message);
        Button placepicker=(Button)findViewById(R.id.placepickerbtn);
        placepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(FormActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        Button addtask=(Button)findViewById(R.id.addtaskbtn);
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(lat==0 && lon==0)
               {
                   Toast.makeText(FormActivity.this,"Location missing",Toast.LENGTH_SHORT).show();
                   return;
               }
               realm.beginTransaction();
               Task t=realm.createObject(Task.class,getKey());
               t.lat=lat; t.lon=lon;
               t.nickname=nickname.getText().toString();
               t.message=message.getText().toString();
               realm.commitTransaction();
               Intent i=new Intent(FormActivity.this,MainActivity.class);
               startActivity(i);
               finish();

               //Toast.makeText(FormActivity.this,"Adding a task here",Toast.LENGTH_SHORT).show();
            }
        });

    }
    int getKey()
    {
        Number n=realm.where(Task.class).max("id");
        if(n==null)
            return 0;

        return  n.intValue()+1;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                if(place.getLatLng()!=null)
                {
                    lat=place.getLatLng().latitude;
                    lon=place.getLatLng().longitude;
                }
                TextView p=(TextView)findViewById(R.id.nickname);
                p.setText(place.getAddress());
            }
        }
    }
}
