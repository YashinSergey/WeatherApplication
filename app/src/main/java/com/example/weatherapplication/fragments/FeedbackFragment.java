package com.example.weatherapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weatherapplication.R;
import com.example.weatherapplication.WeatherActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FeedbackFragment extends Fragment {

    private TextInputEditText textInputEditText;
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
        LinearLayout linearLayout = view.findViewById(R.id.feedback_fragment);
        linearLayout.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.background_day));
        textInputEditText = view.findViewById(R.id.feedback_text);
        send = view.findViewById(R.id.feedback_button);
        send.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!Objects.requireNonNull(textInputEditText.getText()).toString().equals("")) {
                textInputEditText.setText("");
                Toast.makeText(activity.getApplicationContext(), "message sent", Toast.LENGTH_SHORT).show();
                closeVirtualKeyboard(view);
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
