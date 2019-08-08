package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.weatherapplication.fragments.AboutDeveloperFragment;
import com.example.weatherapplication.fragments.SettingsFragment;
import com.example.weatherapplication.fragments.WeatherFragment;
import com.example.weatherapplication.weatherdata.CityPreference;
import com.google.android.material.navigation.NavigationView;

public class WeatherActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String POSITIVE_BUTTON_TEXT = "Ok";

    private CityPreference cityPreference;
    private SettingsFragment settingsFragment;
    private AboutDeveloperFragment aboutDeveloperFragment;
    private WeatherFragment weatherFragment;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityPreference = new CityPreference(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragments(savedInstanceState);
        initSideMenu(toolbar);
    }

    private void initFragments(Bundle savedInstanceState) {
        weatherFragment = new WeatherFragment();
        if (savedInstanceState == null) {
            goToTheFragment(weatherFragment);
        }
        settingsFragment = new SettingsFragment();
        aboutDeveloperFragment = new AboutDeveloperFragment();
    }

    private void initSideMenu(androidx.appcompat.widget.Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.container_for_fragments);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
                goToTheFragment(settingsFragment);
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                goToTheFragment(weatherFragment);
                break;
            case R.id.about_developer:
                goToTheFragment(aboutDeveloperFragment);
                Toast.makeText(this, "is working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                Toast.makeText(this, "WTF?", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.container_for_fragments);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        assert weatherFragment != null;
        weatherFragment.selectCity(city);
        cityPreference.setCity(city);
    }

    private void goToTheFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_for_fragments, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
