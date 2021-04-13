package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.Adapters.DeliveryDetail;
import com.macht.foodowl.Adapters.DeliveryRecyclerAdapter;

public class DeliveryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout AddDeliveryAddressButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DeliveryRecyclerAdapter adapter;
    DeliveryDetail deliveryDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        recyclerView = findViewById(R.id.delivery_recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        SetUpRecyclerView();

    }

    private void SetUpRecyclerView() {
        Query query = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("deliverydetails")
                .orderBy("fullname");
        FirestoreRecyclerOptions<DeliveryDetail> options = new FirestoreRecyclerOptions.Builder<DeliveryDetail>()
                .setQuery(query,DeliveryDetail.class)
                .build();
        adapter = new DeliveryRecyclerAdapter(options, DeliveryActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}