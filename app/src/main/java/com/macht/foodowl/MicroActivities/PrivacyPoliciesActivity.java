package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.macht.foodowl.R;

public class PrivacyPoliciesActivity extends AppCompatActivity {
    LinearLayout PrivacyPolicy, TermsOfUsage, Disclaimer;
    ImageView BackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policies);

        PrivacyPolicy = findViewById(R.id.policy_privacy);
        TermsOfUsage = findViewById(R.id.terms_of_use);
        Disclaimer = findViewById(R.id.disclaimer);
        BackButton = findViewById(R.id.policies_backbutton);

        PrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://adityaasthana1.github.io/foodowl-policies/"));
            startActivity(intent);
        });

        TermsOfUsage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://adityaasthana1.github.io/foodowl-policies/sec"));
            startActivity(intent);
        });

        Disclaimer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://adityaasthana1.github.io/foodowl-policies/tri"));
            startActivity(intent);
        });

        BackButton.setOnClickListener(v -> finish());

    }
}