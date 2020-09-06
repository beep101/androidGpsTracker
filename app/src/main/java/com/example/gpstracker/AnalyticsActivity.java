package com.example.gpstracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Iterator;

public class AnalyticsActivity extends AppCompatActivity implements MapView.OnFirstLayoutListener {

    private GraphView altLineGraph,spdLineGraph;
    private Path path;
    private BoundingBox bb=null;
    private MapView map;
    private TextView infoTxt;
    private SeekBar seekBar;
    private  Marker location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_analytics);

        long pathId=getIntent().getLongExtra("id",-1);
        if(pathId==-1)
            finish();

        path=((TrackerApp)getApplicationContext()).getDataAccess().getPath(pathId);

        Log.println(Log.WARN,"Points saved",String.valueOf(path.getPath().size()));

        infoTxt=findViewById(R.id.infoTxt);
        seekBar=findViewById(R.id.seekBar);

        altLineGraph=findViewById(R.id.altLine);
        spdLineGraph=findViewById(R.id.spdLine);

        DataPoint[] spdData=new DataPoint[path.getPath().size()];
        DataPoint[] altData=new DataPoint[path.getPath().size()];

        int i=0;
        float dist=0;
        for(Point p : path.getPath()){
            dist+=(p.getDist()/1000);
            spdData[i]=new DataPoint(dist,p.getSpeed()*3.6);
            altData[i]=new DataPoint(dist,p.getAlt());
            i++;
        }

        LineGraphSeries<DataPoint> spdLineData=new LineGraphSeries<>(spdData);
        LineGraphSeries<DataPoint> altLineData=new LineGraphSeries<>(altData);

        altLineGraph.getViewport().setScalable(true);
        spdLineGraph.getViewport().setScalable(true);

        altLineGraph.getViewport().setMinX(0);
        altLineGraph.getViewport().setMaxX(dist);
        spdLineGraph.getViewport().setMinX(0);
        spdLineGraph.getViewport().setMaxX(dist);

        altLineGraph.addSeries(altLineData);
        spdLineGraph.addSeries(spdLineData);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        bb=new BoundingBox(latMax(),lonMax(),latMin(),lonMin());
        map=(MapView)findViewById(R.id.map);
        map.addOnFirstLayoutListener(this);
        map.setTileSource(TileSourceFactory.MAPNIK);

        Polyline pathOverlay=new Polyline();
        for(Point p:path.getPath())
            pathOverlay.addPoint(new GeoPoint(p.getLat(),p.getLon()));
        pathOverlay.setColor(Color.BLUE);
        pathOverlay.setWidth(5);
        map.getOverlays().add(pathOverlay);

        Point p=path.getPath().get(0);
        location=new Marker(map);
        location.setPosition(new GeoPoint(p.getLat(),p.getLon()));
        map.getOverlays().add(location);

        infoTxt.setText("Speed: "+String.format("%.1f",p.getSpeed()*3.6)+"kmh Altitude: "+String.format("%.0f",p.getAlt())+"m");

        seekBar.setMax(path.getPath().size()-1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Point p=path.getPath().get(progress);
                location.setPosition(new GeoPoint(p.getLat(),p.getLon()));
                map.invalidate();
                infoTxt.setText("Speed: "+String.format("%.1f",p.getSpeed()*3.6)+"kmh Altitude: "+String.format("%.0f",p.getAlt())+"m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private double latMin(){
        Iterator<Point> it=path.getPath().iterator();
        double min=it.next().getLat();
        while (it.hasNext()) {
            double val=it.next().getLat();
            if (val < min)
                min = val;
        }
        return min;
    }

    private double latMax(){
        Iterator<Point> it=path.getPath().iterator();
        double max=it.next().getLat();
        while (it.hasNext()) {
            double val=it.next().getLat();
            if (val > max)
                max = val;
        }
        return max;
    }

    private double lonMin(){
        Iterator<Point> it=path.getPath().iterator();
        double min=it.next().getLon();
        while (it.hasNext()) {
            double val=it.next().getLon();
            if (val < min)
                min = val;
        }
        return min;
    }

    private double lonMax(){
        Iterator<Point> it=path.getPath().iterator();
        double max=it.next().getLon();
        while (it.hasNext()) {
            double val=it.next().getLon();
            if (val > max)
                max = val;
        }
        return max;
    }

    private double latCenter(){
        return (latMin()+latMax())/2;
    }

    private double lonCenter(){
        return (lonMin()+lonMax())/2;
    }

    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {
        map.zoomToBoundingBox(bb.increaseByScale(1.5f),false);
    }
}
