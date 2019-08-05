package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.weatherapplication.fragments.AboutDeveloperFragment;
import com.example.weatherapplication.fragments.SettingsFragment;
import com.example.weatherapplication.fragments.WeatherFragment;
import com.example.weatherapplication.weatherdata.CityPreference;
import com.google.android.material.navigation.NavigationView;

public class WeatherActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String POSITIVE_BUTTON_TEXT = "Ok";
    private static final String WEATHER_FRAGMENT_TAG = "48df-8c24-8gf9-96n6-se45";

    private CityPreference cityPreference;
    private SettingsFragment settingsFragment;
    private AboutDeveloperFragment aboutDeveloperFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_for_fragments,
                    new WeatherFragment(), WEATHER_FRAGMENT_TAG).commit();
        }
    }

    private void init() {
        cityPreference = new CityPreference(this);
        settingsFragment = new SettingsFragment();
        aboutDeveloperFragment = new AboutDeveloperFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_city:
                showInputDialog();
                return true;
            case R.id.settings:
                goToTheSettings();
                return true;
        }
        return false;
    }



    private void goToTheSettings() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_for_fragments, settingsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_city_dialog));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(POSITIVE_BUTTON_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectCity(input.getText().toString());
            }
        });
        builder.show();
    }

    private void selectCity(String city) {
        WeatherFragment weatherFragment = (WeatherFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_FRAGMENT_TAG);
        weatherFragment.selectCity(city);
        cityPreference.setCity(city);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.about_developer:
                goToAboutDeveloperFragment();
                break;
            case R.id.feedback:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.container_for_fragments);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToAboutDeveloperFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_for_fragments, aboutDeveloperFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
