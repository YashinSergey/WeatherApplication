package com.example.weatherapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherapplication.activities.BaseActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.activities.WeatherActivity;

public class SettingsFragment extends Fragment {

    private BaseActivity baseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TableLayout tableLayout = view.findViewById(R.id.settings_fragment);
        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
        assert weatherActivity != null;
        tableLayout.setBackground(weatherActivity.getApplicationContext().getResources().getDrawable(R.drawable.background_day));
        switchTheme(view);
        switchSensors(view);
        return view;
    }

    private void switchTheme(View view) {
        SwitchCompat switchDarkTheme = view.findViewById(R.id.switch_theme);
        switchDarkTheme.setChecked(baseActivity.isDarkTheme());
        switchDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                baseActivity.setDarkTheme(b);
                baseActivity.recreate();
            }
        });
    }

    private void switchSensors(View view) {
        SwitchCompat switchSensors = view.findViewById(R.id.switch_sensors);
        switchSensors.setChecked(baseActivity.isShowSensors());
        switchSensors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                baseActivity.setVisibleSensors(b);
                baseActivity.recreate();
            }
        });
    }
}
