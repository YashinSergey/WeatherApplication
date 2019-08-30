package com.example.weatherapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weatherapplication.R;
import com.example.weatherapplication.activities.WeatherActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FeedbackFragment extends Fragment {

    private TextInputEditText etTextInput;
    private MaterialButton send;
    private WeatherActivity activity;
    private WeatherFragment weatherFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WeatherActivity) getActivity();
        weatherFragment = new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        setBackground(view);
        initViews(view);
        send.setOnClickListener(listener);
        return view;
    }

    private void initViews(View view) {
        etTextInput = view.findViewById(R.id.feedback_text);
        send = view.findViewById(R.id.feedback_button);
    }

    private void setBackground(View view) {
        LinearLayout linearLayout = view.findViewById(R.id.feedback_fragment);
        if (!activity.isDarkTheme()) {
            linearLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_lite));
        } else {
            linearLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_dark));
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String subject = "Weather app review";
            String address = "yashinsergey125@gmail.com";

            if (!Objects.requireNonNull(etTextInput.getText()).toString().equals("")) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { address});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, etTextInput.getText().toString());
                activity.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
                closeVirtualKeyboard(view);
                etTextInput.setText("");
                replaceFragment(weatherFragment);
            }
        }
    };

    private void closeVirtualKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_for_fragments, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
