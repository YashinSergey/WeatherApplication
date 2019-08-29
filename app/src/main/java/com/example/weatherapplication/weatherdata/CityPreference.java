package com.example.weatherapplication.weatherdata;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    private static final String KEY = "city";
    private static final String MOSCOW = "Moscow";
    private SharedPreferences userPreferences;

    public CityPreference(Activity activity) {
        userPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
            return userPreferences.getString(KEY, MOSCOW);
    }

    public void setCity(String city) {
        userPreferences.edit().putString(KEY, city).apply();
    }
}
