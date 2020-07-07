package com.example.potholedetectiondemo.TAB;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.potholedetectiondemo.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.opencsv.CSVWriter;
import com.pusher.client.Pusher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


public class MapFragment extends Fragment implements SensorEventListener{


    private static final String TAG = "MapFragment";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "MyCsvFile.csv"); // Here csv file name is MyCsvFile.csv
    private CSVWriter writer;
    ArrayList<String[]> data;
    File folder;
    File file;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private Timestamp timestamp;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mlocationCallback;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_CHECK_SETTINGS = 102;
    private Location currentLocation;
    private Location lastLocation;
    private Button btnStart;
    private Button btnStop;

    private LineChart mChart;
    private Pusher pusher;
    private Thread thread;
    private Boolean plotData=true;
    private ImageView imageView;

    private static final float TOTAL_MEMORY = 16.0f;
    private static final float LIMIT_MAX_MEMORY = 12.0f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        data = new ArrayList<String[]>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLastLocation();
        mlocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location userLocation : locationResult.getLocations()) {
                    // Update UI with currentLocation data
                    // ...
                    currentLocation =userLocation;
                    Log.e("CONTINIOUSLOC: ", currentLocation.toString());
                }
            };
        };

        mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        checkLocationSetting(builder);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_map, container, false);

        btnStart=view.findViewById(R.id.btnStart);
        btnStop=view.findViewById(R.id.btnStop);
        mChart= (LineChart) view.findViewById(R.id.chart1);
        //imageView=view.findViewById(R.id.mapIV);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startData();
            }
        });

        btnStop.setClickable(false);
        btnStop.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopData();
            }
        });
        return view;
    }
    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void startData() {
        btnStop.setClickable(true);
        btnStop.setEnabled(true);

        btnStart.setClickable(false);
        btnStart.setEnabled(false);

        sensorManager.registerListener(this, accelerometer, 20000);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stopData() {

        btnStart.setClickable(true);
        btnStart.setEnabled(true);
        btnStop.setClickable(false);
        btnStop.setEnabled(false);
        sensorManager.unregisterListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pothole");
        folder.mkdirs();
        File file = new File(folder, "pothole2.csv");

        if (file.exists()) {
            try {

                writer = new CSVWriter(new FileWriter(file, true));

                Log.d(TAG, "onCreate: try");
            } catch (IOException e) {
                Log.d(TAG, "onCreate: catch");
                e.printStackTrace();
            }
        } else {
            try {

                writer = new CSVWriter(new FileWriter(file));
                writer.writeNext(new String[]{"TIMESTAMP","MILLI", "LATITUDE", "LONGITUDE", "X", "Y", "Z"});
                Log.d(TAG, "onCreate: try");
            } catch (IOException e) {
                Log.d(TAG, "onCreate: catch");
                e.printStackTrace();
            }
        }
//        if(folder.exists()){
//
//            if(file.exists()){
//                try {
//
//                    writer = new CSVWriter(new FileWriter(file,true));
//
//                    Log.d(TAG, "onCreate: try");
//                } catch (IOException e) {
//                    Log.d(TAG, "onCreate: catch");
//                    e.printStackTrace();
//                }
//            }else{
//                try {
//
//                    writer = new CSVWriter(new FileWriter(file));
//                    writer.writeNext(new String[]{"X","Y","Z"});
//                    Log.d(TAG, "onCreate: try");
//                } catch (IOException e) {
//                    Log.d(TAG, "onCreate: catch");
//                    e.printStackTrace();
//                }
//            }
//        }
//        else{
//            folder.mkdirs();
//            if(file.exists()){
//                try {
//
//                    writer = new CSVWriter(new FileWriter(file,true));
//
//                    Log.d(TAG, "onCreate: try");
//                } catch (IOException e) {
//                    Log.d(TAG, "onCreate: catch");
//                    e.printStackTrace();
//                }
//            }else{
//                try {
//
//                    writer = new CSVWriter(new FileWriter(file));
//                    writer.writeNext(new String[]{"X","Y","Z"});
//                    Log.d(TAG, "onCreate: try");
//                } catch (IOException e) {
//                    Log.d(TAG, "onCreate: catch");
//                    e.printStackTrace();
//                }
//            }
//        }

        Log.d(TAG, "onCreate: ");



    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sensorManager.unregisterListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d(TAG, "onSensorChanged: ");
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Isolate the force of gravity with the low-pass filter.
            final double alpha = 0.8;
            double[] gravity = new double[3];
            String[] linear_acceleration = new String[3];
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = String.valueOf(event.values[0] - gravity[0]);
            linear_acceleration[1] = String.valueOf(event.values[1] - gravity[1]);
            linear_acceleration[2] = String.valueOf(event.values[2] - gravity[2]);
            Log.d(TAG, "changes  X:" + linear_acceleration[0] + "  Y: " + linear_acceleration[1] + "   Z:" + linear_acceleration[2]);

            Log.e(TAG, "Accuracy: " + event.accuracy);
            //writer.writeNext(new String[]{linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]});
            timestamp = new Timestamp(System.currentTimeMillis());

            if(currentLocation != null) {
                writer.writeNext(new String[]{timestamp.toString(), String.valueOf(System.currentTimeMillis()),String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), String.valueOf(event.values[0]), String.valueOf(event.values[1]), String.valueOf(event.values[2])});
            }
            else {
                writer.writeNext(new String[]{timestamp.toString(),String.valueOf(System.currentTimeMillis()),String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()), String.valueOf(event.values[0]), String.valueOf(event.values[1]), String.valueOf(event.values[2])});

            }

            makeChart();
            //data.add(linear_acceleration);

            // Log.e(TAG, "acce: " + linear_acceleration );
            //Log.e(TAG, "DATA: "+ data);
//            try {
//                writer = new CSVWriter(new FileWriter(csv));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            ;


            //writer.writeAll(data);


            //writer.writeAll(data); // data is adding to csv

            if (plotData){
                addEntry(event);
                plotData=false;

            }

        }

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            Log.d(TAG, "  X:" + event.values[0] + "  time:" + event.timestamp);


        }

    }

    private void makeChart() {
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("Accelerometer");
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.WHITE);
        LineData line= new LineData();
        line.setValueTextColor(Color.BLACK);

        mChart.setData(line);


        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.RED);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(true);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        feedMultiple();

    }

    private void addEntry(SensorEvent event) {
        LineData data=mChart.getData();

        if(data!=null){
            ILineDataSet set = data.getDataSetByIndex(0);

            if(set == null){
                set= createSet();
                data.addDataSet(set);}
            data.addEntry( new Entry(set.getEntryCount(),event.values[0]+ 5),0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setMaxVisibleValueCount(150);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private ILineDataSet createSet() {
        LineDataSet lineDataSet=new LineDataSet(null,"Dynamic Data");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        return lineDataSet;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void fetchLastLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
//                    Toast.makeText(MainActivity.this, "Permission not granted, Kindly allow permission", Toast.LENGTH_LONG).show();
                showPermissionAlert();
                return;
            }
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location userlocation) {
                        // Got last known currentLocation. In some rare situations this can be null.
                        if (userlocation != null) {
                            // Logic to handle currentLocation object
                            lastLocation =userlocation;
                            Log.e("LAST LOCATION: ", lastLocation.toString()); // You will get your last currentLocation here
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was denied, show alert to explain permission
                    showPermissionAlert();
                }else{
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();
                    }
                }
            }
        }
    }

    private void showPermissionAlert(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All currentLocation settings are satisfied. The client can initialize
                // currentLocation requests here.
                // ...
                startLocationUpdates();
                return;
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Continious Location Request");
                    builder1.setMessage("This request is essential to get currentLocation update continiously");
                    builder1.create();
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            try {
                                resolvable.startResolutionForResult(getActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder1.show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // All currentLocation settings are satisfied. The client can initialize
                // currentLocation requests here.
                startLocationUpdates();
            }
            else {
                checkLocationSetting(builder);
            }
        }
    }
    public void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mlocationCallback,
                null /* Looper */);
    }



    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mlocationCallback);
    }
}