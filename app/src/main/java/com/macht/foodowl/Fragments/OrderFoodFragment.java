package com.macht.foodowl.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.Adapters.FoodItem;
import com.macht.foodowl.Adapters.FoodRecyclerAdapter;
import com.macht.foodowl.CartActivity;
import com.macht.foodowl.R;

public class OrderFoodFragment extends Fragment {
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    FoodRecyclerAdapter foodRecyclerAdapter;
    RecyclerView recyclerView;
    LinearLayout ProceedOrderTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_food,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProceedOrderTextView = view.findViewById(R.id.proceedorderlayout);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("fooditems");
        setUpRecyclerView(view);
        ProceedOrderTextView.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), CartActivity.class) , 1);
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("ORDER_FOOD_FRAGMENT");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        if (isNetworkConnected()){
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }else{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_orderfood, new NetworkErroFragment()).commit();
        }


    }
    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
