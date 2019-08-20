package com.example.weatherapplication.weatherdata.models;

import com.google.gson.annotations.SerializedName;

public class WindModel {
    @SerializedName("speed") public float speed;
    @SerializedName("deg") public float deg;
}
