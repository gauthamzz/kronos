package com.example.phoenix.kronos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.User;
import com.hypertrack.lib.models.UserParams;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import java.util.Date;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    int PLACE_PICKER_REQUEST = 1;
    Realm realm;
    RecyclerView recyclerView;
    ArrayList<Task> tasksList=new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    private PullToRefreshView  mPullToRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm=Realm.getDefaultInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new TasksAdapter(MainActivity.this, tasksList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();


        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

//        mAdapter.notifyItemInserted(tasksList.size() - 1);

        HyperTrack.initialize(MainActivity.this, "pk_test_1665645b7ddebd22fce62d7d0b1d693910daec76");
        checkForLocationSettings();
        UserParams userParams = new UserParams().setName("Nikhil")
                .setPhone("+91-7523979176")
                .setLookupId("+91-7523979176");

        HyperTrack.getOrCreateUser(userParams, new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse response) {
                if (response.getResponseObject() != null) {
                    User user = (User) response.getResponseObject();
                    // Handle user_id, if needed
                    String userId = user.getId();
                    Log.v("anything",userId);
                    Toast.makeText(MainActivity.this, "User created successfully with UserId: " + userId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Handle createUser error here
                Toast.makeText(MainActivity.this, errorResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i=new Intent(MainActivity.this,FormActivity.class);
            startActivity(i);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                checkForLocationSettings();
            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, "Enable Location Services request denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkForLocationSettings() {
        // Check for Location permission
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this, null);
        }

        // Location Permissions and Settings have been enabled
        // Proceed with your app logic here
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void prepareData()
    {
        RealmResults<Task> tasks=realm.where(Task.class).findAll();
        tasksList.addAll(tasks);
        mAdapter.notifyDataSetChanged();

    }



}
