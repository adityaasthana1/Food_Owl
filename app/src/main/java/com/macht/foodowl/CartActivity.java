package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
        collectionReference.document("cartdetails").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()){
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new CartOrderFragment(), "CART_ORDER_FRAGMENT").commit();
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.cart_framelayout,new EmptyCartFragment()).commit();
                        }
                    }
                });


    }
}