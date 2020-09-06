package com.example.gpstracker;

import android.content.Context;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class DataAccess {
    private BoxStore boxStore;
    Box<Path> pathBox;

    public DataAccess(Context context){
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
        pathBox=boxStore.boxFor(Path.class);
    }

    public void addPath(Path path){
        pathBox.put(path);
    }

    public Path getPath(long id){
        return pathBox.get(id);
    }

    public List<Path> getAllPaths(){
        return pathBox.getAll();
    }
}
