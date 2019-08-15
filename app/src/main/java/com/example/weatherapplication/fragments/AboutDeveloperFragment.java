package com.example.weatherapplication.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.weatherapplication.R;
import com.example.weatherapplication.WeatherActivity;

public class AboutDeveloperFragment extends Fragment {

    private WeatherActivity activity;
    private TextView textView;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WeatherActivity) getActivity();
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_developer, container, false);
        textView = view.findViewById(R.id.about_developer_textView);
        linearLayout = view.findViewById(R.id.about_developer_fragment);
        linearLayout.setBackgroundResource(R.drawable.background_day);
        if (activity.isDarkTheme()) {
            textView.setTextColor(Color.WHITE);
        }
        return view;
    }
}
