package com.macht.foodowl.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.Adapters.CartDetails;
import com.macht.foodowl.Adapters.CartElement;
import com.macht.foodowl.Adapters.CartRecyclerAdapter;
import com.macht.foodowl.Adapters.FoodItem;
import com.macht.foodowl.Adapters.FoodRecyclerAdapter;
import com.macht.foodowl.R;

import java.util.Map;

public class CartOrderFragment extends Fragment {
    RecyclerView recyclerView;
    CartRecyclerAdapter cartRecyclerAdapter;
    CollectionReference CartListReference;
    DocumentReference CartDetailsReference;
    FirebaseAuth firebaseAuth;
    LinearLayout PlaceOrderLayout;
    Map<String, CartElement> FinalCart;
    int GrandFinalAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        CartListReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                .document("cartlist").collection("list");
        CartDetailsReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart").document("cartdetails");
        PlaceOrderLayout = view.findViewById(R.id.placeorderlayout);
        SetUpCartRecycletView(view);
        PlaceOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalCart = cartRecyclerAdapter.getCartList();
                GrandFinalAmount = cartRecyclerAdapter.getGrandFinalAmount();

            }
        });

    }

    private void SetUpCartRecycletView(@NonNull View view) {
        Query query = CartListReference.orderBy("quantity", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CartElement> options = new FirestoreRecyclerOptions.Builder<CartElement>()
                .setQuery(query, CartElement.class)
                .build();
        cartRecyclerAdapter = new CartRecyclerAdapter(options,view,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartRecyclerAdapter);
        Log.d("ADAPTER","CART RECYCLER ADAPTER Created SUccessfully");
    }

    @Override
    public void onStart() {
        super.onStart();
        cartRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cartRecyclerAdapter.stopListening();
    }
}
