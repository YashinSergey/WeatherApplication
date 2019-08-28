package com.example.weatherapplication.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.weatherapplication.R;
import com.example.weatherapplication.activities.WeatherActivity;
import com.facebook.drawee.view.SimpleDraweeView;

public class AboutDeveloperFragment extends Fragment {

    private WeatherActivity activity;
    private TextView textView;
    private SimpleDraweeView myPhoto;
    private RelativeLayout relativeLayout;

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
        init(view);

        relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_lite));
        if (activity.isDarkTheme()) {
            textView.setTextColor(Color.WHITE);
            relativeLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_dark));
        }
        return view;
    }

    private void init(View view) {
        textView = view.findViewById(R.id.about_developer_textView);
        relativeLayout = view.findViewById(R.id.about_developer_fragment);
        myPhoto = view.findViewById(R.id.avatar_image_about_developer);
        myPhoto.setActualImageResource(R.mipmap.avatar);
    }
}
