package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Button RazorPayButton;
    TextView OrderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Map<String,OrderAdapter> OrderList = getIntent().getParcelableExtra("ORDER_MAP");
        int orderAmount = getIntent().getIntExtra("TOTAL_AMOUNT", 0);
        RazorPayButton = findViewById(R.id.razorpaybutton);
        OrderValue = findViewById(R.id.OrderValue);

        OrderValue.setText(Integer.toString(orderAmount));

        RazorPayButton.setOnClickListener(v -> {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_nRLHL0Vjnxh6ej");

            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("name","Food Owl");
                jsonObject.put("description", "Testing the payment feature of our application.");
                jsonObject.put("theme.color", R.color.red);
                jsonObject.put("currency" , "INR");
                jsonObject.put("amount",orderAmount*100);
                jsonObject.put("prefill.contact","9752778817");
                jsonObject.put("prefill.email","asthana.aditya1@gmail.com");
                checkout.open(PaymentActivity.this,jsonObject);

            }catch (Exception e){

            }
        });


    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed.", Toast.LENGTH_SHORT).show();
    }
}