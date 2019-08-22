package com.example.weatherapplication.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapplication.database.DBHelper;
import com.example.weatherapplication.weatherdata.CityPreference;
import com.example.weatherapplication.R;
import com.example.weatherapplication.activities.WeatherActivity;
import com.example.weatherapplication.weatherdata.WeatherData;
import com.example.weatherapplication.weatherdata.models.WeatherRequestModel;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = "WeatherFragment";
    private static final String FONT_FILENAME = "fonts/weather.ttf";
    
    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView dateTextView;
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

    private DBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WeatherActivity) getActivity();
        assert activity != null;
        weatherFont = Typeface.createFromAsset(activity.getAssets(), FONT_FILENAME);
        dbHelper = new DBHelper(activity.getApplicationContext());
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
        dateTextView = rootView.findViewById(R.id.date_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon);
        roomTemperature = rootView.findViewById(R.id.room_temperature);
        roomHumidity = rootView.findViewById(R.id.room_humidity);
        relativeLayout = rootView.findViewById(R.id.weather_fragment);
    }

    private void updateWeatherData(final String city) {
        WeatherData.getWeatherData().getAPI().loadWeather(city,
                getString(R.string.open_weather_map_key), "metric")
                .enqueue(new Callback<WeatherRequestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestModel> call,
                                           @NonNull Response<WeatherRequestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            renderWeather(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestModel> call, @NonNull Throwable t) {
                        Toast.makeText(activity.getApplicationContext(), getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                        Log.e("RESPONSE", getString(R.string.network_error));
                    }
                });
    }

    private void addToDB(WeatherRequestModel model) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CITY_NAME, cityTextView.getText().toString());
        cv.put(DBHelper.DATE, dateTextView.getText().toString());
        cv.put(DBHelper.TEMP, currentTemperatureTextView.getText().toString());
        cv.put(DBHelper.WIND_SPEED, detailsTextView.getText().toString());
        cv.put(DBHelper.PRESSURE, model.mainModel.pressure);
        cv.put(DBHelper.HUMIDITY, model.mainModel.humidity);
        db.insert(DBHelper.CITIES_TABLE, null, cv);
        dbHelper.close();
    }

    private void renderWeather(WeatherRequestModel model) {
        Log.d(LOG_TAG, "model " + model.toString());
        setPlaceName(model.name, model.sysModel.country);
        setDetails(model.weatherModel[0].description, model.windModel.speed, model.mainModel.humidity, model.mainModel.pressure);
        setCurrentTemp(model.mainModel.temp);
        setWeatherIcon(model.weatherModel[0].id, model.sysModel.sunrise * 1000, model.sysModel.sunset * 1000);
        setDate(model);
        addToDB(model);
    }

    private void setDate(WeatherRequestModel model) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        String date = dateFormat.format(new Date(model.dt * 1000));
        dateTextView.setText(date);
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        long currentTime = new Date().getTime();
        if (currentTime >= sunrise && currentTime < sunset) {
            relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_day));
        } else {
            relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_day)); //Todo replace background
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

    private void setCurrentTemp(float temp) {
        String currentTextText = String.format(Locale.getDefault(), "%.0f", temp) + " \u2103";
        currentTemperatureTextView.setText(currentTextText);
    }

    private void setDetails(String description, float windSpeed, float humidity, float pressure)  {
        String detailsText = description.toUpperCase() + "\n"
                + "Wind speed: " + windSpeed + " m/s\n"
                + "Humidity: " + (int)humidity + "%\n"
                + "Pressure: " + (int)(pressure * 0.75006375541921f) + " mmHg";
        detailsTextView.setText(detailsText);
    }

    private void setPlaceName(String name, String country) {
        String cityText = name.toUpperCase() + ", " + country;
        cityTextView.setText(cityText);
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
