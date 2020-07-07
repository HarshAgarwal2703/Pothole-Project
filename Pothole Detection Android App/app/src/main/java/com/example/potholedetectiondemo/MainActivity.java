package com.example.potholedetectiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

public class MainActivity extends AppCompatActivity  {

    private static final int PERMISSION_REQUEST_CODE = 1;

    GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isGPS = false;
    String latitude;
    String longitude;
    String locationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }


}
