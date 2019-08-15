package com.example.weatherapplication.fragments;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapplication.weatherdata.CityPreference;
import com.example.weatherapplication.R;
import com.example.weatherapplication.activities.WeatherActivity;
import com.example.weatherapplication.weatherdata.WeatherDataLoader;

import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = "WeatherFragment";
    private static final String FONT_FILENAME = "fonts/weather.ttf";

    private Handler handler = new Handler();

    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView updatedTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;

    private TextView roomTemperature;
    private TextView roomHumidity;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private SensorManager sensorManager;
    private RelativeLayout relativeLayout;
    private WeatherActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WeatherActivity) getActivity();
        assert activity != null;
        weatherFont = Typeface.createFromAsset(activity.getAssets(), FONT_FILENAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        initViews(rootView);
        weatherIcon.setTypeface(weatherFont);
        updateWeatherData(new CityPreference(activity).getCity());
        getSensors();
        return rootView;
    }

    private void initViews(View rootView) {
        cityTextView = rootView.findViewById(R.id.city_field);
        updatedTextView = rootView.findViewById(R.id.update_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon);
        roomTemperature = rootView.findViewById(R.id.room_temperature);
        roomHumidity = rootView.findViewById(R.id.room_humidity);
        relativeLayout = rootView.findViewById(R.id.weather_fragment);
    }

    private void updateWeatherData(final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(getActivity(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }).start();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void renderWeather(JSONObject json) {
        Log.d(LOG_TAG, "json " + json.toString());
        try {
            cityTextView.setText(json.getString("name").toUpperCase(Locale.US) + " ,"
                    + json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsTextView.setText(details.getString("description").toUpperCase(Locale.US) + "\n" + "Humidity: "
                    + main.getString("humidity") + "%" + "\n" + "Pressure: "
                    + String.format("%.0f mmHg",main.getDouble("pressure") * 0.75006375541921));
            currentTemperatureTextView.setText(String.format("%.0f", main.getDouble("temp") - 273.15) + "\u00b0C");

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            String updatedOn = dateFormat.format(new Date(json.getLong("dt") * 1000));
            updatedTextView.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.d(LOG_TAG, "One ore more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        long currentTime = new Date().getTime();
        if (currentTime >= sunrise && currentTime < sunset) {
            relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_day));
        } else {
            relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_day));; //Todo replace background
        }
        Log.d(LOG_TAG, "id " + id);
        switch (id) {
            case 2:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_thunder);
                break;
            case 3:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_drizzle);
                break;
            case 5:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_rainy);
                break;
            case 6:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_snowy);
                break;
            case 7:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_foggy);
                break;
            case 8:
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_cloudy);
                break;
            default:
                break;
        }
        weatherIcon.setText(icon);
    }

    public void selectCity(String city) {
        updateWeatherData(city);
    }

    private void getSensors() {
        if (activity.isShowSensors()) {
            sensorManager = (SensorManager) Objects.requireNonNull(getActivity()).getSystemService(SENSOR_SERVICE);

            assert sensorManager != null;
            sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity.isShowSensors()) {
            sensorManager.registerListener(sensorsListener, sensorTemperature, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorsListener, sensorHumidity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (activity.isShowSensors()) {
            sensorManager.unregisterListener(sensorsListener, sensorTemperature);
            sensorManager.unregisterListener(sensorsListener, sensorHumidity);
        }
    }

    private SensorEventListener sensorsListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor == sensorHumidity){
                showRoomIndicator(sensorEvent, roomHumidity,  "Room humidity", "%");
            }
            if (sensorEvent.sensor == sensorTemperature) {
                showRoomIndicator(sensorEvent, roomTemperature, "Room temperature", "\u00b0C");
            }
        }
    };

    private void showRoomIndicator(SensorEvent sensorEvent, TextView view, String subscription, String symbol) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(subscription).append("\n").append(sensorEvent.values[0])
                .append(symbol);
        view.setText(strBuilder);
    }
}
