package com.macht.foodowl.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.Adapters.FoodItem;
import com.macht.foodowl.Adapters.FoodRecyclerAdapter;
import com.macht.foodowl.R;

public class OrderFoodFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    FoodRecyclerAdapter foodRecyclerAdapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_food,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("fooditems");
        setUpRecyclerView(view);

    }

    private void setUpRecyclerView(@NonNull View view) {
        Query query = collectionReference.orderBy("foodtag",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FoodItem> options = new FirestoreRecyclerOptions.Builder<FoodItem>()
                .setQuery(query, FoodItem.class)
                .build();
        foodRecyclerAdapter = new FoodRecyclerAdapter(getContext(), options, Glide.with(this), view);
        recyclerView = view.findViewById(R.id.FoodItemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foodRecyclerAdapter);
        Log.d("ADAPTER","ADAPTER Created SUccessfully");

    }

    @Override
    public void onStart() {
        super.onStart();

        foodRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        foodRecyclerAdapter.stopListening();
    }
}
