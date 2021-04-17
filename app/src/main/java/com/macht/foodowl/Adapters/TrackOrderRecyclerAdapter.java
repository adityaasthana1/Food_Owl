package com.macht.foodowl.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.macht.foodowl.R;
import com.macht.foodowl.models.CartElement;

public class TrackOrderRecyclerAdapter extends FirestoreRecyclerAdapter <CartElement, TrackOrderRecyclerAdapter.TrackOrderRecyclerViewHolder>{

    String Rupee;

    public TrackOrderRecyclerAdapter(@NonNull FirestoreRecyclerOptions<CartElement> options) {
        super(options);
        Rupee = "₹";
    }

    @Override
    protected void onBindViewHolder(@NonNull TrackOrderRecyclerViewHolder holder, int position, @NonNull CartElement model) {
        String ordername = model.getFoodname() + " x" + model.getQuantity();
        String pricecalculated = Rupee +  model.getPrice() + " x " + model.getQuantity() + " = " + model.getValue();
        holder.OrderElementName.setText(ordername);
        holder.PriceCalculated.setText(pricecalculated);
    }

    @NonNull
    @Override
    public TrackOrderRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_cart_trackorder, parent, false);
        return new TrackOrderRecyclerViewHolder(view);
    }

    public class TrackOrderRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView OrderElementName, PriceCalculated;
        public TrackOrderRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderElementName = itemView.findViewById(R.id.order_element_name);
            PriceCalculated = itemView.findViewById(R.id.order_item_total_price);
        }
    }


}
