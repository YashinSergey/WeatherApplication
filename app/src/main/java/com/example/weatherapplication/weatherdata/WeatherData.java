package com.example.weatherapplication.weatherdata;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherData {

    private static WeatherData weatherData = null;
    private IWeatherData API;

    private WeatherData() {
        API = createAdapter();
    }

    public static WeatherData getWeatherData() {
        if (weatherData == null) {
            weatherData = new WeatherData();
        }
        return weatherData;
    }

    private IWeatherData createAdapter() {
        Retrofit adapter = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        return adapter.create(IWeatherData.class);
    }

    public IWeatherData getAPI() {
        return API;
    }
}
