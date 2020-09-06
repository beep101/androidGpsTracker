package com.example.gpstracker;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Path{
    @Id
    private long id;
    private String name;
    private ToMany<Point> path;

    public Path(){
    }

    public Path(Collection<Location> locations){
        Iterator<Location> it=locations.iterator();
        while(it.hasNext())
            path.add(new Point((Location)it));
    }

    public void add(Point p){
        if(path.isEmpty())
            p.setDist(0);
        else
            p.setDist(p.distance(path.get(path.size()-1)));
        path.add(p);
    }

    public void add(Location l){
        Point p=new Point(l);
        add(p);
    }

    public float distance(){
        Iterator<Point> it=path.iterator();
        if(it.hasNext())
            it.next();
        float distance=0;
        while(it.hasNext())
            distance+=it.next().getDist();
        return path.get(path.size()-1).getDist();
    }

    public float maxSpeed(){
        Iterator<Point> it=path.iterator();
        float maxSpeed=0;
        while(it.hasNext()){
            float temp=it.next().getSpeed();
            if(temp>maxSpeed)
                maxSpeed=temp;
        }
        return maxSpeed;
    }

    public float avgSpeed(){
        float avg=0;
        for(Point p:path)
            avg+=p.getSpeed();
        avg/=path.size();
        return avg;
    }

    public double maxAltitude(){
        Iterator<Point> it=path.iterator();
        double maxAlt=-10000;
        while(it.hasNext()){
            double temp=it.next().getAlt();
            if(temp>maxAlt)
                maxAlt=temp;
        }
        return maxAlt;
    }

    public double minAltitude(){
        Iterator<Point> it=path.iterator();
        double minAlt=10000;
        while(it.hasNext()){
            double temp=it.next().getAlt();
            if(temp<minAlt)
                minAlt=temp;
        }
        return minAlt;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Point> getPath() {
        return path;
    }

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public void setId(long id){this.id=id;}

    public long getId(){return id;}

}
