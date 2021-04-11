package com.macht.foodowl.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.macht.foodowl.R;

public class FoodRecyclerAdapter extends FirestoreRecyclerAdapter<FoodItem, FoodRecyclerAdapter.FoodViewHolder> {

    private final RequestManager glide;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    Context context;
    View fragmentView;
    TextView priceView;

    public FoodRecyclerAdapter(Context context ,@NonNull FirestoreRecyclerOptions<FoodItem> options, RequestManager glide, View FragmentView) {
        super(options);
        this.glide = glide;
        this.context = context;
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.fragmentView = FragmentView;
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull FoodItem model) {
        String priceholder = "₹" + model.getFoodprice();
        holder.FoodName.setText(model.getFoodname());
        holder.FoodPrice.setTextColor(Color.parseColor("#000000"));
        holder.FoodPrice.setText(priceholder);
        holder.FoodDescription.setText(model.getFooddescription());
        if (model.getFoodtag().equals("none")){
            holder.FoodTag.setText("");
            holder.FoodTag.setHeight(0);
            holder.FoodTag.setBackgroundResource(R.drawable.white_nowhere);
        }else{
            holder.FoodTag.setText(model.getFoodtag());
        }
        if(model.getFoodavailability().equals("no")){
            holder.AddToCartButton.setBackgroundColor(Color.parseColor("#909090"));
            holder.AddToCartButton.setText("Unavailable");
        }else{
            holder.AddToCartButton.setOnClickListener(v -> {
                String price = "INR (Rs) "+ "₹"+model.getFoodprice();
                priceView.setText(price);
                Toast.makeText(context, "Clicked on " + model.getFoodname(), Toast.LENGTH_SHORT).show();
            });
        }
        holder.FoodTag.setText(model.getFoodtag());
        storageReference.child("foodimages").child(model.getFoodid())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        glide.load(uri).into(holder.imageView);
                    }
                });


    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_element,parent,false);
        return new FoodViewHolder(v);
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView FoodName,FoodDescription,FoodPrice,FoodTag;
        Button AddToCartButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.food_element_image);
            FoodName = itemView.findViewById(R.id.element_food_name);
            FoodDescription = itemView.findViewById(R.id.food_element_description);
            FoodPrice = itemView.findViewById(R.id.food_element_price);
            FoodTag = itemView.findViewById(R.id.food_element_tag);
            AddToCartButton = itemView.findViewById(R.id.food_element_button);
            priceView = fragmentView.findViewById(R.id.ordertotaltext);



        }
    }


}
