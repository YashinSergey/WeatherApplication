package com.example.weatherapplication.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import com.example.weatherapplication.R;

import java.io.IOException;
import java.util.List;


public class CoordinatesAct extends Activity {

    public final static String MSG_NO_DATA = "No data";
    public static String CITY_NAME;
    public static boolean START;

    private Location loc;
    private LocationManager locationManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        START = true;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setLocation();
        setCityName();
        goToWeatherActivity();
    }

    private void goToWeatherActivity() {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }

    private void setCityName() {
        if (loc != null) {
            CITY_NAME = getAddressByLoc(loc);
        } else {
            CITY_NAME = MSG_NO_DATA;
        }
    }

    private void setLocation() {
        checkPermission();
        loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private String getAddressByLoc(Location loc) {
        final Geocoder geo = new Geocoder(this);

        List<Address> list;
        try {
            list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

        if (list.isEmpty()) return MSG_NO_DATA;
        Address a = list.get(0);
        final int index = a.getMaxAddressLineIndex();
        String postal = null;
        if (index >= 0) {
            postal = a.getLocality();
        }
        return postal;
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }
}
