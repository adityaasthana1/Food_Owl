package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.macht.foodowl.Fragments.FoodLoadingFragment;
import com.macht.foodowl.Fragments.NetworkErroFragment;
import com.macht.foodowl.Fragments.OrderFoodFragment;

public class OrderFoodActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    public final static int LOAD_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);
        frameLayout = findViewById(R.id.framelayout_orderfood);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_orderfood,new FoodLoadingFragment()).commit();
        if (isNetworkConnected()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_orderfood,new OrderFoodFragment(), "ORDER_FOOD_FRAGMENT").commit();
                }
            },LOAD_TIME);

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_orderfood,new NetworkErroFragment()).commit();
        }


    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}