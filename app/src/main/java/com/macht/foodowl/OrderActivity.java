package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.Fragments.NetworkErroFragment;
import com.macht.foodowl.Fragments.NetworkErrorStaticFragment;
import com.macht.foodowl.Fragments.OrderFragment;

public class OrderActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        frameLayout = findViewById(R.id.myorders_framelayout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (isNetworkConnected()){
            getSupportFragmentManager().beginTransaction().replace(R.id.myorders_framelayout, new OrderFragment()).commitAllowingStateLoss();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.myorders_framelayout, new NetworkErrorStaticFragment()).commitAllowingStateLoss();
        }


    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}