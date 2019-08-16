package com.example.weatherapplication.weatherdata.models;

import com.google.gson.annotations.SerializedName;

public class CoordModel {
    @SerializedName("lon") public float lon;
    @SerializedName("lat") public float lat;
}
