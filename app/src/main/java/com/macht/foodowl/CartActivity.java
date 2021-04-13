package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.macht.foodowl.Adapters.CartLoadingFragment;
import com.macht.foodowl.Fragments.CartOrderFragment;
import com.macht.foodowl.Fragments.EmptyCartFragment;
import com.macht.foodowl.Fragments.FoodLoadingFragment;
import com.macht.foodowl.Fragments.NetworkErroFragment;

import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    CollectionReference collectionReference;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.cart_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        frameLayout = findViewById(R.id.cart_framelayout);

        getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new CartLoadingFragment()).commit();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart");

        if (isNetworkConnected()){
            collectionReference.document("cartdetails").get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()){
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new CartOrderFragment(), "CART_ORDER_FRAGMENT").commit();
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new EmptyCartFragment()).commit();
                        }
                    });
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout, new NetworkErroFragment());
        }



    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}