package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.Fragments.CartLoadingFragment;
import com.macht.foodowl.Fragments.CartOrderFragment;
import com.macht.foodowl.Fragments.EmptyCartFragment;
import com.macht.foodowl.Fragments.NetworkErroFragment;
import com.macht.foodowl.Fragments.NetworkErrorStaticFragment;

import java.io.IOException;

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
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new CartOrderFragment(), "CART_ORDER_FRAGMENT").commitAllowingStateLoss();
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new EmptyCartFragment()).commitAllowingStateLoss();
                        }
                    });
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout, new NetworkErrorStaticFragment()).commitAllowingStateLoss();
        }



    }
    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}