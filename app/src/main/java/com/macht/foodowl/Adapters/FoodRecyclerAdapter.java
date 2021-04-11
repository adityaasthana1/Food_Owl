package com.macht.foodowl.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;
import com.macht.foodowl.R;

import java.util.Date;
import java.util.HashMap;

public class FoodRecyclerAdapter extends FirestoreRecyclerAdapter<FoodItem, FoodRecyclerAdapter.FoodViewHolder> {

    private final RequestManager glide;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    CollectionReference CartReference;
    Context context;
    View fragmentView;
    TextView priceView;
    HashMap<String,CartElement> Cart;
    int TotalAmount;
    CartDetails cartDetails;

    public FoodRecyclerAdapter(Context context ,@NonNull FirestoreRecyclerOptions<FoodItem> options, RequestManager glide, View FragmentView) {
        super(options);
        this.glide = glide;
        this.context = context;
        this.storageReference = FirebaseStorage.getInstance().getReference();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.CartReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart");
        this.fragmentView = FragmentView;
        this.Cart = new HashMap<>();
        this.priceView = FragmentView.findViewById(R.id.ordertotaltext);
        FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                .document("cartdetails").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult().exists()) {
                                cartDetails = task.getResult().toObject(CartDetails.class);
                                TotalAmount = cartDetails.getTotalamount();
                            }else{
                                TotalAmount = 0;
                            }
                        }else{

                        }
                        String ordertotal = "INR ₹" + TotalAmount;
                        priceView.setText(ordertotal);
                    }
                });

    }

    @Override
    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull FoodItem model) {
        Log.d("ADDING_ITEM", model.getFoodname());
        CartReference.document("cartlist").collection("list").document(model.getFoodid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            CartElement cartElement = task.getResult().toObject(CartElement.class);
                            String added = "Added : " + cartElement.getQuantity();
                            holder.AddToCartButton.setText(added);
                        }else{
                            Log.d("GETTING_QUANTITY","There was problem getting the quantity of items  present in the cart");
                        }
                    }
                });
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
            holder.AddToCartButton.setBackgroundResource(R.drawable.buttoncanceled);
            holder.AddToCartButton.setText("Unavailable");
        }else{
            holder.AddToCartButton.setOnClickListener(v -> {
                AddElementToCart(holder, model);
                String price = "INR (Rs) "+ "₹"+model.getFoodprice();
                priceView.setText(price);
                //Toast.makeText(context, "Clicked on " + model.getFoodname() + "Quantity : " + Integer.toString(QuantitySelected), Toast.LENGTH_SHORT).show();
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

    private void AddElementToCart(@NonNull FoodViewHolder holder,FoodItem foodmodel) {
        NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMaxValue(6);
        numberPicker.setMinValue(1);
        numberPicker.setValue(1);
        final int[] selectedquantity = {0};

        AlertDialog.Builder QuantityDialog = new AlertDialog.Builder(context);
        QuantityDialog.setTitle("Quantity");
        QuantityDialog.setMessage("Select the quantity of the food.");
        QuantityDialog.setView(numberPicker);
        QuantityDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedquantity[0] = numberPicker.getValue();

                CartReference.document("cartlist").collection("list").document(foodmodel.getFoodid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful() && task.getResult().exists()){
                                    int existingquantity = 0, existingvalue=0;
                                    CartElement cartElement = task.getResult().toObject(CartElement.class);
                                    existingquantity = cartElement.getQuantity();
                                    existingquantity+=selectedquantity[0];
                                    existingvalue = cartElement.getValue();
                                    existingvalue+=selectedquantity[0]*Integer.parseInt(foodmodel.getFoodprice());
                                    TotalAmount += selectedquantity[0]*Integer.parseInt(foodmodel.getFoodprice());
                                    CartDetails cartDetails = new CartDetails(TotalAmount,new Date().toString());
                                    CartElement cartElement1 = new CartElement(foodmodel.getFoodid(),foodmodel.foodname,foodmodel.getFoodprice(),existingquantity,existingvalue);
                                    CartReference.document("cartlist").collection("list").document(foodmodel.getFoodid())
                                            .set(cartElement1);
                                    CartReference.document("cartdetails").set(cartDetails);
                                    String added = "Added : " + existingquantity;
                                    holder.AddToCartButton.setText(added);
                                    Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show();
                                }else {
                                    TotalAmount += selectedquantity[0]*Integer.parseInt(foodmodel.getFoodprice());
                                    String price = "INR ₹" + TotalAmount;
                                    String added = "Added : " + selectedquantity[0];
                                    holder.AddToCartButton.setText(added);
                                    CartElement tempElement = new CartElement(foodmodel.getFoodid(),foodmodel.foodname,foodmodel.getFoodprice(),selectedquantity[0]);
                                    CartDetails cartDetails = new CartDetails(TotalAmount,new Date().toString() );
                                    Cart.put(foodmodel.getFoodid(),tempElement);
                                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                                            .document("cartlist").collection("list").document(foodmodel.getFoodid()).set(tempElement);
                                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                                            .document("cartdetails").set(cartDetails);
                                    Toast.makeText(context, "Added To Cart!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        QuantityDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        QuantityDialog.show();


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
