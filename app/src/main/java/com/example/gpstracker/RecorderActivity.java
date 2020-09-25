package com.example.gpstracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecorderActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;

    private TextView infoTxt;
    private TextView spdTxt,altTxt;
    private Button recordButton;
    private RecorderService recorderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recorder);

        infoTxt=findViewById(R.id.infoTxt);
        spdTxt=findViewById(R.id.spdTxt);
        altTxt=findViewById(R.id.altTxt);
        recordButton=findViewById(R.id.recordBtn);

        recorderService=((TrackerApp)getApplicationContext()).getRecorderService();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, recorderService);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        }catch(SecurityException e){
            finish();
        }

        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            enableRecording();
        else
            disableRecording();

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recorderService.isInProgress()){
                    stop();
                }else{
                    start();
                }
            }
        });
    }

    public void start(){
        recordButton.setBackgroundColor(Color.RED);
        recordButton.setText("Stop");
        recorderService.startRecording();
    }

    public void stop(){
        recorderService.stopRecording();
        final Path path= recorderService.getPath();
        if(!path.getPath().isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save the record");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name="Path";
                    name=input.getText().toString();
                    path.setName(name);
                    ((TrackerApp)getApplicationContext()).getDataAccess().addPath(path);
                    Log.println(Log.WARN,"Data points recorded",String.valueOf(path.getPath().size()));
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }else{
            Toast toast=Toast.makeText(getApplicationContext(),"Record is empty",Toast.LENGTH_LONG);
            toast.show();
        }
        recordButton.setText("Start");
        recordButton.setBackgroundColor(Color.GREEN);
        spdTxt.setText("/ kmh");
        altTxt.setText("/ m");

    }

    public void updateCurrentParameters(float speed, double altitude){
        speed*=3.6;
        spdTxt.setText(String.format("%.1f",speed)+" kmh");
        altTxt.setText(String.format("%.0f",altitude)+" m");
    }

    @Override
    public void onLocationChanged(Location location) {
        updateCurrentParameters(location.getSpeed(),location.getAltitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        enableRecording();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if(recorderService.isInProgress())
            stop();
        disableRecording();
    }


    public void enableRecording(){
        recordButton.setEnabled(true);
        recordButton.setBackgroundColor(Color.GREEN);
        recordButton.setText("Start");
        infoTxt.setText("Ready to record");
    }

    public void disableRecording(){
        recordButton.setEnabled(false);
        recordButton.setBackgroundColor(Color.GRAY);
        recordButton.setText("Start");
        infoTxt.setText("Location service or GPS turned off");
    }
}