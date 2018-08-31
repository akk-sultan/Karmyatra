package com.example.rahul_jareda.karmyatra;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.Locale;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.splunk.mint.Mint;

import org.w3c.dom.Text;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static androidx.work.NetworkType.CONNECTED;


public class MainActivity extends AppCompatActivity implements SharedCoordinates {

    private double mLastKnownLat, mLastKnownLong;
    private TextView startValueLocation;
    private TextView startValueTime;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String DATABASE_NAME="id6827146_movements";
    public static AppDatabase movementDatabase;
    private LocationCallback mLocationCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this.getApplication(), "003641e6");
        setContentView(R.layout.activity_main);
        startValueLocation = findViewById(R.id.StartValueLocation);
        startValueTime = findViewById(R.id.StartValueTime);
        movementDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //build workmanager to upload saved movements in local database
        Constraints localDbUploadConstraints = new Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build();
        PeriodicWorkRequest.Builder localDbUploadBuilder = new PeriodicWorkRequest.Builder(SyncWorker.class, 1, TimeUnit.MINUTES);
        PeriodicWorkRequest localDbUploadWork = localDbUploadBuilder
                .setConstraints(localDbUploadConstraints)
                .build();
        WorkManager.getInstance().enqueue(localDbUploadWork);
    }
    /****
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        final TextView startText = findViewById(R.id.StartControllerTrip);
        CharSequence startTextChar = startText.getText();
        savedInstanceState.putCharSequence("savedStartText", startTextChar);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        final TextView startText = findViewById(R.id.StartControllerTrip);
        CharSequence startTextChar = savedInstanceState.getCharSequence("savedStartText");
        startText.setText(startTextChar);
    }****/

    public void startLocationUpdates(View view) {

        Button button = (Button) view;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            showAlert();
        }
        if (button.getText().equals(getResources().getString(R.string.started)))
            return;
        else if (button.getText().equals(getResources().getString(R.string.start)))
            button.setText(R.string.started);
        long UPDATE_INTERVAL = 15*60*1000;  /* 15 minutes */
        long FASTEST_INTERVAL = 14*60*1000; /* 14 minutes */


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // do work here
                Location location = locationResult.getLastLocation();
                mLastKnownLat = location.getLatitude();
                mLastKnownLong = location.getLongitude();
                final Date date = new Date(location.getTime());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
                final String time = dateFormat.format(date);
                startValueTime.setText(time);

                //for volley library
                User logged_user = SharedPrefManager.getUser();
                final String mobile_vol = logged_user.getMobile();
                final String latitude_vol = String.valueOf(mLastKnownLat);
                final String longitude_vol = String.valueOf(mLastKnownLong);
                final String time_vol = time;
                Movements movement = new Movements();
                movement.setMobile(mobile_vol);
                movement.setLatitude(latitude_vol);
                movement.setLongitude(longitude_vol);
                movement.setTime(time_vol);
                Boolean insertDbRequired = true;

                insertDbOrServer.insertDbOrServer_method(movement, insertDbRequired, getApplicationContext());

                int LocationListNumber =  findCurrentLocationID.findCurrentLocationID_method(mLastKnownLat,mLastKnownLong) ;
                switch (LocationListNumber){
                    case 0 :
                        startValueLocation.setText("Pipraigaon");
                        break;
                    case 1 :
                        startValueLocation.setText("Rahatwas");
                        break;
                    case 2:
                        startValueLocation.setText("Orr");
                        break;
                    case 3:
                        startValueLocation.setText("Hinotia Pipalkhera");
                        break;
                    case 4:
                        startValueLocation.setText("Ashok Nagar");
                        break;
                    case 5:
                        startValueLocation.setText("Ratikheda");
                        break;
                    case 6:
                        startValueLocation.setText("Shadoragaon");
                        break;
                    case 7:
                        startValueLocation.setText("Pilighat");
                        break;
                    case 8:
                        startValueLocation.setText("Pagara");
                        break;
                    case 9:
                        startValueLocation.setText("Maban");
                        break;
                    case 10:
                        startValueLocation.setText("Guna");
                        break;
                    case 11:
                        startValueLocation.setText("Mahughara");
                        break;
                    case 12:
                        startValueLocation.setText("Ruthiyai");
                        break;
                    case 13:
                        startValueLocation.setText("Vijaipur");
                        break;
                    case 14:
                        startValueLocation.setText("Khumbraj");
                        break;
                    case 15:
                        startValueLocation.setText("Chachora Binaganj");
                        break;
                    case 16:
                        startValueLocation.setText("Sinduriya Kachari");
                        break;
                    case 17:
                        startValueLocation.setText("Beavra Rajgarh");
                        break;
                    case 18:
                        startValueLocation.setText("Pachor Road");
                        break;
                    case 19:
                        startValueLocation.setText("Parhana Mau");
                        break;
                    case 20:
                        startValueLocation.setText("Sarangpur");
                        break;
                    case 21:
                        startValueLocation.setText("Shajapur");
                        break;
                    case 22:
                        startValueLocation.setText("Maksi");
                        break;
                    case 23:
                        startValueLocation.setText("Miyana");
                        break;
                    case 24:
                        startValueLocation.setText("Badarwas");
                        break;
                    case 25:
                        startValueLocation.setText("Kolaras");
                        break;
                    case 26:
                        startValueLocation.setText("Shivpuri");
                        break;
                    case 27:
                        startValueLocation.setText("Padarkhera");
                        break;
                    case 28:
                        startValueLocation.setText("Mohana");
                        break;
                    case 29:
                        startValueLocation.setText("Ghatigaon");
                        break;
                    case 30:
                        startValueLocation.setText("Panihar");
                        break;
                    case 31:
                        startValueLocation.setText("Gwalior");
                        break;
                    case 32:
                        startValueLocation.setText("Habibganj");
                        break;
                    case 33:
                        startValueLocation.setText("Bhopal");
                        break;
                    default:
                        startValueLocation.setText(latitude_vol+" , "+longitude_vol);
                        break;
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,Looper.myLooper());
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Kindly Enable required permissions to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void endTrip(View view) {
        Button button = findViewById(R.id.StartControllerTrip);
        if (button.getText().equals(getResources().getString(R.string.started)))
            button.setText(R.string.start);
        if(mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onBackPressed(){
        this.moveTaskToBack(true);
    }

    void Log_Out(View view){
        endTrip(view);
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }
}