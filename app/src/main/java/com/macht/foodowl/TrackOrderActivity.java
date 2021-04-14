package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TrackOrderActivity extends AppCompatActivity {

    TextView OrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        String order_id = getIntent().getStringExtra("ORDER_ID");
        OrderId = findViewById(R.id.orderid);
        OrderId.setText(order_id);

    }
}