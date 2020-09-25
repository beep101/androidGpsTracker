package com.example.gpstracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class RecorderService extends Service implements LocationListener {
    private static final String CHANNEL_ID = "LocationTrackerService";

    private final IBinder binder=new RecorderServiceBinder();
    public class RecorderServiceBinder extends Binder {
        RecorderService getService() {
            return RecorderService.this;
        }
    }

    private Path path;
    private boolean inProgress=false;

    public RecorderService(){

    }

    //SERVICE INTERFACE
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        createNotificationChanel();
        Intent notificationIntent=new Intent(this,RecorderActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("GPS Tracker")
                .setContentText("Work in progress...")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    private void createNotificationChanel(){
        NotificationChannel serviceNotificationChanel=new NotificationChannel(
                CHANNEL_ID,
                "Location tracking service channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager=getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceNotificationChanel);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //RECORDING COMMANDS
    public void startRecording(){
        path=new Path();
        inProgress=true;
    }

    public void stopRecording(){
        inProgress=false;
    }

    //LOCATION LISTENER INTERFACE
    @Override
    public void onLocationChanged(Location location) {
        if(inProgress) {
            path.add(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    //GETTERS & SETTERS
    public boolean isInProgress() {
        return inProgress;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
