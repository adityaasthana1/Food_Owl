package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.macht.foodowl.R;

public class GetSupportActivity extends AppCompatActivity {

    TextView PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_support);
        PhoneNumber = findViewById(R.id.phonenumber_support);

        /*
        PhoneNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9752778817"));
            startActivity(intent);
        });
        */

    }
}