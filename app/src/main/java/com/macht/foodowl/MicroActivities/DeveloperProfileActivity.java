package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.macht.foodowl.R;

public class DeveloperProfileActivity extends AppCompatActivity {
    LinearLayout LinkedIn, Instagram, Facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_profile);

        LinkedIn = findViewById(R.id.developer_linkedin);
        Instagram = findViewById(R.id.developer_instagram);
        Facebook = findViewById(R.id.developer_facebook);

        LinkedIn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/adityaasthana2018/"));
            startActivity(intent);
        });

        Instagram.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/aditya.asthanaa/"));
            startActivity(intent);
        });

        Facebook.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aditya.asthana.56/"));
            startActivity(intent);
        });
    }
}