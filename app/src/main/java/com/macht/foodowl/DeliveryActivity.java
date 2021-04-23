package com.macht.foodowl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.models.DeliveryDetail;
import com.macht.foodowl.Adapters.DeliveryRecyclerAdapter;

public class DeliveryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout AddDeliveryAddressButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DeliveryRecyclerAdapter adapter;
    DeliveryDetail deliveryDetail;
    ImageView backbutton;
    public static final int ADD_DELIVERY_ADDRESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        recyclerView = findViewById(R.id.delivery_recyclerview);
        AddDeliveryAddressButton = findViewById(R.id.adddeliveryaddress);
        backbutton = findViewById(R.id.deliverybackbutton);

        backbutton.setOnClickListener(v -> finish());

        AddDeliveryAddressButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(DeliveryActivity.this , AddDeliveryAddressActivity.class) , ADD_DELIVERY_ADDRESS);
        });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            deliveryDetail = data.getParcelableExtra("ADDED_DELIVERY_ADDRESS");
            Intent intent = new Intent();
            intent.putExtra("NEW_DELIVERY_ADDRESS" , deliveryDetail);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
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