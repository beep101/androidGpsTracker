package com.example.gpstracker;

import android.location.Location;
import android.location.LocationManager;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Point {
    @Id
    private long id;
    private double alt,lat,lon;
    private float speed;
    private long time;
    private float dist;

    public Point(){

    }

    public Point(Location loc){
        time=loc.getTime();
        speed=loc.getSpeed();
        lat=loc.getLatitude();
        lon=loc.getLongitude();
        alt=loc.getAltitude();
    }

    public float distance(Point p){
        Location a=new Location(LocationManager.GPS_PROVIDER);
        a.setLongitude(getLon());
        a.setLatitude(getLat());
        a.setAltitude(getAlt());

        Location b=new Location(LocationManager.GPS_PROVIDER);
        b.setLongitude(p.getLon());
        b.setLatitude(p.getLat());
        b.setAltitude(p.getAlt());

        return a.distanceTo(b);
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public void setId(long id){this.id=id;}

    public long getId(){return id;}
}
