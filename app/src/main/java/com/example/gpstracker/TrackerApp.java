package com.example.gpstracker;

import android.app.Application;
import android.provider.ContactsContract;

public class TrackerApp extends Application {
    private DataAccess dataAccess;

    @Override
    public void onCreate() {
        super.onCreate();
        dataAccess=new DataAccess(this);
    }

    public DataAccess getDataAccess(){
        return dataAccess;
    }
}
