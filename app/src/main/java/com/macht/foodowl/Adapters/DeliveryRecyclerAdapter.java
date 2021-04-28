package com.macht.foodowl.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;
import com.macht.foodowl.models.DeliveryDetail;

public class DeliveryRecyclerAdapter extends FirestoreRecyclerAdapter<DeliveryDetail, DeliveryRecyclerAdapter.DeliveryRecyclerViewHolder> {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DeliveryDetail CurrentDetail;
    Context context;
    AlertDialog.Builder builder;

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
        holder.ParentLayout.setOnClickListener(v->{});
        holder.linearLayout.setOnClickListener(v -> firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
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
                }));

        holder.DeliveryElementDelete.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(context,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
            builder.setTitle("Delete Address");
            builder.setMessage("Are you sure, you want to delete this address?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                firebaseFirestore.collection("users")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .collection("deliverydetails")
                        .document(model.getAddressid())
                        .delete()
                        .addOnCompleteListener(task -> {
                            Toast.makeText(context, "Address deleted successfully.", Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("No", (dialog, which) -> {

            });

            builder.show();


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
        LinearLayout linearLayout;

        public DeliveryRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            DeliveryElementName = itemView.findViewById(R.id.element_name);
            DeliveryElementAddress = itemView.findViewById(R.id.element_fulladdress);
            DeliveryElementPincode = itemView.findViewById(R.id.element_pincode);
            DeliveryElementDelete = itemView.findViewById(R.id.delete_delivery_icon);
            ParentLayout = itemView.findViewById(R.id.list_element_parent);
            linearLayout = itemView.findViewById(R.id.delivery_address_container);

        }
    }


}
