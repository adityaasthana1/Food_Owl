package com.macht.foodowl.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.macht.foodowl.models.OrderAdapter;
import com.macht.foodowl.Adapters.OrderRecyclerAdapter;
import com.macht.foodowl.R;

public class OrderFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    OrderRecyclerAdapter orderRecyclerAdapter;


    public OrderFragment(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.orders_recyclerview);
        setUpRecyclerView(view);


    }

    void setUpRecyclerView(@NonNull View view){
        Query query = firebaseFirestore.collection("orders")
                    .whereEqualTo("user_id", firebaseAuth.getCurrentUser().getUid())
                    .orderBy("order_status");
        FirestoreRecyclerOptions<OrderAdapter> options = new FirestoreRecyclerOptions.Builder<OrderAdapter>()
                .setQuery(query, OrderAdapter.class)
                .build();

        orderRecyclerAdapter = new OrderRecyclerAdapter(options,getContext(),view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(orderRecyclerAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        orderRecyclerAdapter.startListening();
    }


}
