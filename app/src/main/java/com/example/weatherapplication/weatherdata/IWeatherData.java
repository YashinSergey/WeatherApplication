package com.example.weatherapplication.weatherdata;

import com.example.weatherapplication.weatherdata.models.WeatherRequestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherData {

    @GET("data/2.5/weather")
    Call<WeatherRequestModel> loadWeather(@Query("q") String city,
                                          @Query("appid") String keyApi,
                                          @Query("units")String units);
}
