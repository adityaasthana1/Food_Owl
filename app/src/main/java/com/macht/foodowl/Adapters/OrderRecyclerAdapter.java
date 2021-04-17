package com.macht.foodowl.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.macht.foodowl.R;
import com.macht.foodowl.TrackOrderActivity;
import com.macht.foodowl.models.CartElement;
import com.macht.foodowl.models.DeliveryDetail;
import com.macht.foodowl.models.OrderAdapter;

public class OrderRecyclerAdapter extends FirestoreRecyclerAdapter <OrderAdapter, OrderRecyclerAdapter.OrderRecyclerViewHolder> {

    Context context;
    View view;
    FirebaseFirestore firebaseFirestore;

    public OrderRecyclerAdapter(@NonNull FirestoreRecyclerOptions<OrderAdapter> options, Context context, View view) {
        super(options);
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderRecyclerViewHolder holder, int position, @NonNull OrderAdapter model) {
        final String[] final_name = {""};
        firebaseFirestore.collection("orders")
                .document(model.getOrderid())
                .collection("cartlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()){
                            CartElement cartElement = snapshot.toObject(CartElement.class);
                            final_name[0] += cartElement.getQuantity() + "x" + cartElement.getFoodname() + " | ";
                        }
                        holder.FullOrderString.setText(final_name[0]);
                        DeliveryDetail deliveryDetail = model.getDeliveryDetail();
                        String full_address = deliveryDetail.getFullname() + ", " + deliveryDetail.getHousenumber() + ", " + deliveryDetail.getArea()
                                + ", " + deliveryDetail.getState();
                        holder.OrderAddress.setText(full_address);
                        holder.OrderId.setText(model.getOrderid());
                        if (model.getOrder_state().equals("active")){
                            holder.OrderStatus.setTextColor(Color.parseColor("#2ecc71"));
                        }else {
                            holder.OrderStatus.setTextColor(Color.parseColor("#848484"));
                        }
                        String order_status = "Order Status : " + model.getOrder_status().toUpperCase();
                        holder.OrderStatus.setText(order_status);
                    }
                });
            holder.ContainerLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, TrackOrderActivity.class);
                intent.putExtra("ORDER", model);
                context.startActivity(intent);
            });


    }

    @NonNull
    @Override
    public OrderRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.order_element_layout, parent,false);
        return new OrderRecyclerViewHolder(view);
    }

    public class OrderRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView FullOrderString, OrderId, OrderStatus, OrderAddress;
        LinearLayout ContainerLayout;
        public OrderRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            FullOrderString = itemView.findViewById(R.id.order_full_name);
            OrderId = itemView.findViewById(R.id.order_id);
            OrderStatus = itemView.findViewById(R.id.order_status);
            OrderAddress = itemView.findViewById(R.id.order_full_address);
            ContainerLayout = itemView.findViewById(R.id.order_element_container);
        }
    }


}
