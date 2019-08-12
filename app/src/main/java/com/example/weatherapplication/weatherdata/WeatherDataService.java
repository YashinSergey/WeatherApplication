package com.example.weatherapplication.weatherdata;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.weatherapplication.R;
import com.example.weatherapplication.fragments.WeatherFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDataService extends Service {

    private static final String OPEN_WEATHER_MAP_API ="https://api.openweathermap.org/data/2.5/weather?q=%s&appid=";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int FINE = 200;

    WeatherFragment weatherFragment;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        weatherFragment = new WeatherFragment();
        weatherFragment.updateWeatherData(WeatherFragment.cityPref);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static JSONObject getJSONData(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API,city));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, context.getString(R.string.open_weather_map_key));
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String temporaryStr;
            while ((temporaryStr = reader.readLine()) != null) {
                rawData.append(temporaryStr).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE) != FINE) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null; //TODO обработать исключение
        }
    }
}
