package com.example.gpstracker;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

public class TrackerApp extends Application {
    private DataAccess dataAccess;
    private RecorderService recorderService;
    private ServiceConnection recorderServiceConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecorderService.RecorderServiceBinder binder=(RecorderService.RecorderServiceBinder)service;
            recorderService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        dataAccess=new DataAccess(this);
        Intent startRecorderService=new Intent(this,RecorderService.class);
        ContextCompat.startForegroundService(this,startRecorderService);
        bindService(startRecorderService,recorderServiceConn, Context.BIND_AUTO_CREATE);
    }

    public DataAccess getDataAccess(){
        return dataAccess;
    }
    public RecorderService getRecorderService(){return recorderService;}
}
