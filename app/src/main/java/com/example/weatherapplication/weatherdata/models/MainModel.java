package com.example.weatherapplication.weatherdata.models;

import com.google.gson.annotations.SerializedName;

public class MainModel {
    @SerializedName("temp") public float temp;
    @SerializedName("pressure") public float pressure;
    @SerializedName("humidity") public float humidity;
    @SerializedName("temp_min") public float tempMin;
    @SerializedName("temp_max") public float tempMax;
}
