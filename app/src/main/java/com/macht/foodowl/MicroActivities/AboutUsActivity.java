package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.macht.foodowl.R;

public class AboutUsActivity extends AppCompatActivity {
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        backButton = findViewById(R.id.aboutus_backbutton);

        backButton.setOnClickListener(v -> finish());
    }
}