package com.example.weatherapplication.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapplication.R;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String THEME_SHARED_PREFERENCE = "THEME";
    private static final String IS_DARK_THEME = "IS_DARK_THEME";
    private static final String SENSORS_SHARED_PREFERENCE = "SENSORS";
    private static final String IS_SHOW_SENSORS = "IS_SHOW_SENSORS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDarkTheme()) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    public boolean isDarkTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_DARK_THEME, true);
    }

    public void setDarkTheme(boolean isDarkTheme) {
        SharedPreferences sharedPreferences = getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DARK_THEME, isDarkTheme);
        editor.apply();
    }

    public boolean isShowSensors() {
        SharedPreferences sharedPreferences = getSharedPreferences(SENSORS_SHARED_PREFERENCE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_SHOW_SENSORS, false);
    }

    public void setVisibleSensors(boolean isShowSensors) {
        SharedPreferences sharedPreferences = getSharedPreferences(SENSORS_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SHOW_SENSORS, isShowSensors);
        editor.apply();
    }
}
