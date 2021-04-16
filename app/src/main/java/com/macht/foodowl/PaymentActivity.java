package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.macht.foodowl.Adapters.CartDetails;
import com.macht.foodowl.Adapters.CartElement;
import com.macht.foodowl.Adapters.DeliveryDetail;
import com.macht.foodowl.Adapters.OrderAdapter;
import com.macht.foodowl.Adapters.UserDataAdapter;
import com.macht.foodowl.Adapters.UserOrder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    LottieAnimationView lottieAnimationView;
    UserDataAdapter userDataAdapter;
    int orderAmount, TotalAmount, deliveryAmount;
    Map<String, CartElement> FinalCart;
    DeliveryDetail FinalDelivery;
    TextView TitleText,DescriptionText;
    public static final int RESPONSE_DELAY = 4000;
    String message;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        TitleText = findViewById(R.id.title_text);
        DescriptionText = findViewById(R.id.description_text);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (!isNetworkConnected()){
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        
        lottieAnimationView = findViewById(R.id.payment_animation);
        lottieAnimationView.setAnimation("payment_processing.json");
        lottieAnimationView.setRepeatCount(10);
        lottieAnimationView.playAnimation();
        
        //TotalAmount = getIntent().getIntExtra("TOTAL_AMOUNT", 0);
        orderAdapter = getIntent().getParcelableExtra("ORDER_OBJECT");
        orderAmount = orderAdapter.getAmount_paid();


        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()){
                        userDataAdapter = task.getResult().toObject(UserDataAdapter.class);
                        Checkout checkout = new Checkout();
                        checkout.setKeyID("rzp_test_nRLHL0Vjnxh6ej");
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("name","Food Owl");
                            jsonObject.put("description", "Ordering Food!");
                            jsonObject.put("theme.color", R.color.red);
                            jsonObject.put("currency" , "INR");
                            jsonObject.put("amount",orderAmount*100);
                            jsonObject.put("prefill.name", userDataAdapter.getFname()+userDataAdapter.getLname());
                            jsonObject.put("prefill.email",userDataAdapter.getEmail());
                            jsonObject.put("image", "https://raw.githubusercontent.com/adityaasthana1/Food_Owl/master/app/src/main/res/drawable-v24/foodowllogo.png");
                            checkout.open(PaymentActivity.this,jsonObject);
                        }catch (Exception e){
                            Log.d("PAYMENT_ACTIVITY_JSON" , e.getMessage());
                        }
                    }
                });

        //Map<String,OrderAdapter> OrderList = getIntent().getParcelableExtra("ORDER_MAP");



    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        lottieAnimationView.setAnimation("payment_success.json");
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();
        TitleText.setText("Payment Successful!");
        DescriptionText.setText("");
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                orderAdapter.setOrderid(s);
                UserOrder userOrder = new UserOrder(s,"placed",orderAmount,ServerValue.TIMESTAMP);
                firebaseFirestore.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("orders")
                        .document(s)
                        .set(userOrder);

                firebaseFirestore.collection("orders")
                        .document(s)
                        .set(orderAdapter);

                firebaseFirestore.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("cart")
                        .document("cartlist")
                        .collection("list")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        CartElement cartElement = snapshot.toObject(CartElement.class);
                                        Toast.makeText(PaymentActivity.this, "Placing order", Toast.LENGTH_SHORT).show();
                                        firebaseFirestore.collection("orders")
                                                .document(s)
                                                .collection("cartlist")
                                                .document(cartElement.getFoodid())
                                                .set(cartElement);
                                        firebaseFirestore.collection("users")
                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                .collection("cart")
                                                .document("cartlist")
                                                .collection("list")
                                                .document(cartElement.getFoodid())
                                                .delete();

                                    }
                                    firebaseFirestore.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .collection("cart")
                                            .document("cartdetails")
                                            .delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("CONFIRMATION", "TRUE");
                                                    setResult(Activity.RESULT_OK, intent);
                                                    Toast.makeText(PaymentActivity.this, "Order Placed, Sending Intent", Toast.LENGTH_SHORT).show();
                                                    Intent intent1 = new Intent(PaymentActivity.this , TrackOrderActivity.class);
                                                    intent1.putExtra("ORDER", orderAdapter);
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                            });

                                }

                            }
                        });
                           
            }
        }, RESPONSE_DELAY);

    }

    @Override
    public void onPaymentError(int i, String s) {
        //Toast.makeText(this, "Payment Failed.", Toast.LENGTH_SHORT).show();
        lottieAnimationView.setAnimation("payment_fail.json");
        lottieAnimationView.setRepeatCount(1);
        lottieAnimationView.playAnimation();
        TitleText.setText("Payment Failed!");
        DescriptionText.setText(s);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(PaymentActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                String error_message = s;
                String confirmation = "FALSE";

                switch (i){
                    case Checkout.PAYMENT_CANCELED:
                        message = "Payment Cancelled by User";
                        DescriptionText.setText(message);
                        break;
                    case Checkout.NETWORK_ERROR:
                        message = "Payment was failed due to Network Error.";
                        DescriptionText.setText(message);
                        break;
                }
                intent.putExtra("CONFIRMATION" , confirmation);
                intent.putExtra("MESSAGE", message  );
                setResult(Activity.RESULT_OK,intent);
                finish();


            }
        },RESPONSE_DELAY);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}