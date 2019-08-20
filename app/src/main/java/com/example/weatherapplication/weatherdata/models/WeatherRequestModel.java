package com.example.weatherapplication.weatherdata.models;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestModel {
    @SerializedName("coord") public CoordModel coordModel;
    @SerializedName("weather") public WeatherModel[] weatherModel;
    @SerializedName("base") public String base;
    @SerializedName("main") public MainModel mainModel;
    @SerializedName("visibility") public int visibility;
    @SerializedName("wind") public WindModel windModel;
    @SerializedName("clouds") public CloudsModel cloudsModel;
    @SerializedName("dt") public long dt;
    @SerializedName("sys") public SysModel sysModel;
    @SerializedName("timezone") public int timezone;
    @SerializedName("id") public long id;
    @SerializedName("name") public String name;
    @SerializedName("cod") public int cod;
}
