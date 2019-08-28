package com.example.weatherapplication.activities;

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

import com.example.weatherapplication.R;
import com.example.weatherapplication.fragments.AboutDeveloperFragment;
import com.example.weatherapplication.fragments.FeedbackFragment;
import com.example.weatherapplication.fragments.SettingsFragment;
import com.example.weatherapplication.fragments.WeatherFragment;
import com.example.weatherapplication.weatherdata.CityPreference;
import com.google.android.material.navigation.NavigationView;

public class WeatherActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String POSITIVE_BUTTON_TEXT = "Ok";

    private CityPreference cityPreference;
    private SettingsFragment settingsFragment;
    private AboutDeveloperFragment aboutDeveloperFragment;
    private FeedbackFragment feedbackFragment;
    private WeatherFragment weatherFragment;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityPreference = new CityPreference(this);
        initToolbar();
        initFragments(savedInstanceState);
        initSideMenu();
    }

    private void initFragments(Bundle savedInstanceState) {
        weatherFragment = new WeatherFragment();
        if (savedInstanceState == null) {
            replaceFragment(weatherFragment);
        }
        settingsFragment = new SettingsFragment();
        aboutDeveloperFragment = new AboutDeveloperFragment();
        feedbackFragment = new FeedbackFragment();
    }

    private void initSideMenu() {
        drawer = findViewById(R.id.drawer_layout);
        drawer.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.background_lite));
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.background_lite));
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_city) {
            showInputDialog();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                replaceFragment(weatherFragment);
                break;
            case R.id.about_developer:
                replaceFragment(aboutDeveloperFragment);
                break;
            case R.id.feedback:
                replaceFragment(feedbackFragment);
                break;
            case R.id.settings:
                replaceFragment(settingsFragment);
                break;
        }
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_for_fragments, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
