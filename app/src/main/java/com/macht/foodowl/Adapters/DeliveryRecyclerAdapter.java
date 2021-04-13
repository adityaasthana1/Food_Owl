package com.macht.foodowl.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;

public class DeliveryRecyclerAdapter extends FirestoreRecyclerAdapter<DeliveryDetail, DeliveryRecyclerAdapter.DeliveryRecyclerViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DeliveryDetail CurrentDetail;
    Context context;

    public DeliveryRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DeliveryDetail> options, Context context) {
        super(options);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DeliveryRecyclerViewHolder holder, int position, @NonNull DeliveryDetail model) {
        holder.DeliveryElementName.setText(model.getFullname());
        holder.DeliveryElementAddress.setText(model.getFulladdress());
        holder.DeliveryElementPincode.setText(model.getPincode());
        holder.DeliveryElementName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("currentdetail")
                        .document("currentaddress")
                        .set(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = ((Activity)context).getIntent();
                                CurrentDetail = model;
                                String fulladdress = model.getFullname() + ", " + model.getHousenumber() + ", " + model.getArea() + ", " + model.getCity() + ", " + model.getState();
                                CurrentDetail.setFulladdress(fulladdress);
                                intent.putExtra("NEW_DELIVERY_ADDRESS", CurrentDetail);
                                ((Activity)context).setResult(Activity.RESULT_OK, intent);
                                ((Activity)context).finish();
                            }
                        });

            }
        });
    }

    @NonNull
    @Override
    public DeliveryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item_element,parent,false);
        return new DeliveryRecyclerViewHolder(view);
    }

    public DeliveryDetail getDeliveryDetails() {
        return CurrentDetail;
    }

    public static class DeliveryRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView DeliveryElementName,DeliveryElementAddress, DeliveryElementPincode;
        Button DeliveryElementDelete;
        RelativeLayout ParentLayout;

        public DeliveryRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            DeliveryElementName = itemView.findViewById(R.id.element_name);
            DeliveryElementAddress = itemView.findViewById(R.id.element_fulladdress);
            DeliveryElementPincode = itemView.findViewById(R.id.element_pincode);
            DeliveryElementDelete = itemView.findViewById(R.id.element_delete);
            ParentLayout = itemView.findViewById(R.id.list_element_parent);
        }
    }


}
