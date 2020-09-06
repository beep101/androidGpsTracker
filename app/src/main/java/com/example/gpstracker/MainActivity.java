package com.example.gpstracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.*;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    DataAccess dataAccess;

    private Button recordBtn;

    private ListView fileList;
    private ArrayAdapter<Path> recordsListAdapter;

    private List<Path> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //*******************
        //On first run
        //Setting permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
            },1);
        }
        //*******************

        dataAccess=((TrackerApp)getApplicationContext()).getDataAccess();

        recordBtn=findViewById(R.id.recordBtn);

        //record list setup
        fileList=findViewById(R.id.fileList);
        recordsList=new ArrayList<>();
        recordsListAdapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recordsList);
        fileList.setAdapter(recordsListAdapter);

        recordBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecorder();
            }
        });

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAnalytics(recordsListAdapter.getItem(position));
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        for(int res:grantResults)
            if(res!=PackageManager.PERMISSION_GRANTED)
                finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords();
    }

    public void loadRecords(){
        recordsList.clear();
        List<Path> paths=dataAccess.getAllPaths();
        recordsList.addAll(paths);
        recordsListAdapter.notifyDataSetChanged();
    }

    public void startRecorder(){
        Intent intent = new Intent(this, RecorderActivity.class);
        startActivity(intent);
    }

    public void startAnalytics(Path record){
            Intent intent = new Intent(this, AnalyticsActivity.class);
            intent.putExtra("id", record.getId());
            startActivity(intent);
    }

}