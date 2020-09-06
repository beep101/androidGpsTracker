package com.example.gpstracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class Recorder implements LocationListener {
    private Path path;
    private boolean inProgress=false;


    public Recorder(){

    }

    public void startRecording(){
        path=new Path();
        inProgress=true;
    }

    public void stopRecording(){
        inProgress=false;
    }

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
