package com.example.phoenix.kronos;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PerformAction {
    private Context context;
    private final  int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public  PerformAction(Context context) {
        this.context = context;
    }
    public void performWifiAction(String option) {   //working fine
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        switch (option) {
            case "0":
                assert wifiManager != null;
                wifiManager.setWifiEnabled(true);
                break;
            case "1":
                assert wifiManager != null;
                wifiManager.setWifiEnabled(false);
                break;
            case "2":
                assert wifiManager != null;
                boolean wifiEnabled = wifiManager.isWifiEnabled();
                Toast.makeText(context, String.valueOf(wifiEnabled), Toast.LENGTH_SHORT).show();
                if (wifiEnabled)
                    wifiManager.setWifiEnabled(false);
                else
                    wifiManager.setWifiEnabled(true);
                break;
        }
    }
    public void performProfileAction(String option) {  //working on redmi
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(option.equals("0")){
            assert audioManager != null;
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        else if(option.equals("1")){
            if (audioManager != null) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        }
        else if(option.equals("2")){
            assert audioManager != null;
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public void performWallpaperAction(Bitmap bitmap) {
        WallpaperManager wallManager = WallpaperManager.getInstance(context);
        try {
            wallManager.clear();
            if(bitmap != null)
                wallManager.setBitmap(bitmap);
        } catch (IOException ex) {}
    }

    public void performLoadAppAction(String packageName) {
        Log.v("My",packageName);
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            Toast.makeText(context, "Package not found " + packageName, Toast.LENGTH_SHORT).show();
        }
    }

    public void performNotifyAppAction(String title,String message) { //working fine
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_phone_icon)
                        .setContentTitle(title)
                        .setContentText(message);

        // Sets an ID for the notification
        int mNotificationId = 101;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        assert mNotifyMgr != null;
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
    public void performMessageAppAction(int i, String phoneNo, String message) {
        // Here, thisActivity is the current activity
        //   send sms
        if(i == 0) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(context.getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context.getApplicationContext(), ex.getMessage(),
                        Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }
    public void peformSpeakerPhoneAction() {
        AudioManager audioManager =  (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(true);
        Log.v("entered","entered");
    }
    public void performVolumeAction(int alarm, int media, int ring) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (1.5*media), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, (int) (1.5*alarm), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, (int) (1.5*ring), 0);
    }


    public void performDownloadAction(String url) {
        if (ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        // Here, thisActivity is the current activity
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
        final String[] separated = url.split("/");
        final String myFile = separated[separated.length - 1];
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Some description");
        request.setTitle("Some title");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, myFile);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        assert manager != null;
        manager.enqueue(request);
    }
}