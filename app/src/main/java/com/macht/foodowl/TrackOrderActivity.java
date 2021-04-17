package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.models.CartElement;
import com.macht.foodowl.Adapters.DeliveryDetail;
import com.macht.foodowl.models.OrderAdapter;
import com.macht.foodowl.Adapters.TrackOrderRecyclerAdapter;

public class TrackOrderActivity extends AppCompatActivity {
    OrderAdapter orderAdapter;
    TextView OrderStatusText, OrderStatusDescription, OrderIdText, OrderDate, OrderItemTotal, OrderDeliveryCharge, OrderFinalPrice, DeliveryName, DeliveryFullAddress, DeliveryPhoneNumber;
    RecyclerView TrackOrderRecyclerView;
    LottieAnimationView lottieAnimationView;
    TrackOrderRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        orderAdapter = getIntent().getParcelableExtra("ORDER");

        OrderStatusText = findViewById(R.id.order_status_text);
        OrderStatusDescription = findViewById(R.id.order_status_description);
        OrderIdText = findViewById(R.id.track_order_id);
        OrderDate = findViewById(R.id.date_placed);
        OrderItemTotal = findViewById(R.id.order_item_total_text);
        OrderDeliveryCharge = findViewById(R.id.order_delivery_charges_text);
        OrderFinalPrice = findViewById(R.id.order_final_value_text);
        DeliveryName = findViewById(R.id.delivery_name);
        DeliveryPhoneNumber = findViewById(R.id.delivery_phone);
        DeliveryFullAddress = findViewById(R.id.delivery_address);
        lottieAnimationView = findViewById(R.id.track_order_animation);
        TrackOrderRecyclerView = findViewById(R.id.track_order_recyclerview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        SetUpRecyclerView();

        String status = "";
        if (orderAdapter.getOrder_status().equals("placed")){
            status = "Order Placed";
            OrderStatusText.setTextColor(Color.parseColor("#2ecc71"));
            OrderStatusText.setText(status);
            OrderStatusDescription.setText(R.string.order_placed);
            lottieAnimationView.setAnimation("orderplaced.json");
            lottieAnimationView.setRepeatCount(0);
        }else if (orderAdapter.getOrder_status().equals("accepted")){
            status = "Order Accepted";
            OrderStatusText.setText(status);
            OrderStatusDescription.setText(R.string.order_accepeted);
            OrderStatusText.setTextColor(Color.parseColor("#2ecc71"));
            lottieAnimationView.setAnimation("preparing.json");
        }else if (orderAdapter.getOrder_status().equals("onway")){
            status = "On the way!";
            OrderStatusText.setText(status);
            OrderStatusDescription.setText(R.string.order_onway);
            OrderStatusText.setTextColor(Color.parseColor("#e74c3c"));
            lottieAnimationView.setAnimation("scooter.json");
            lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        }else if (orderAdapter.getOrder_status().equals("delivered")){
            status = "Delivered!";
            OrderStatusText.setText(status);
            OrderStatusDescription.setText(R.string.order_delivered);
            OrderStatusText.setTextColor(Color.parseColor("#848484"));
            lottieAnimationView.setAnimation("done.json");
            lottieAnimationView.setRepeatCount(0);
        }

        String orderid = "Order ID : " + orderAdapter.getOrderid();
        OrderIdText.setText(orderid);

        String time = "Date Placed : " + orderAdapter.getTime();
        OrderDate.setText(time);


        String rupee = "₹";
        String itemtotal = rupee + orderAdapter.getItem_total();
        OrderItemTotal.setText(itemtotal);

        String deliverycharge = rupee + orderAdapter.getDelivery_amount();
        OrderDeliveryCharge.setText(deliverycharge);

        String final_amount = rupee + orderAdapter.getAmount_paid();
        OrderFinalPrice.setText(final_amount);

        DeliveryDetail deliveryDetail = orderAdapter.getDeliveryDetail();

        DeliveryName.setText(deliveryDetail.getFullname());

        String deliveryaddress = deliveryDetail.getHousenumber() + ", " + deliveryDetail.getArea() + ", " + deliveryDetail.getCity() + ", " + deliveryDetail.getState() + ", " + deliveryDetail.getPincode();
        DeliveryFullAddress.setText(deliveryaddress);

        DeliveryPhoneNumber.setText(deliveryDetail.getMobilenumber());

    }

    private void SetUpRecyclerView() {
        Query query = firebaseFirestore.collection("orders")
                .document(orderAdapter.getOrderid())
                .collection("cartlist")
                .orderBy("foodname");
        FirestoreRecyclerOptions<CartElement> options = new FirestoreRecyclerOptions.Builder<CartElement>()
                .setQuery(query, CartElement.class)
                .build();
        adapter = new TrackOrderRecyclerAdapter(options);
        TrackOrderRecyclerView.setAdapter(adapter);
        TrackOrderRecyclerView.setHasFixedSize(true);
        TrackOrderRecyclerView.setNestedScrollingEnabled(true);
        TrackOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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