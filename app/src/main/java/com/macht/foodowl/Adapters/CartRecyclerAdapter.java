package com.macht.foodowl.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartRecyclerAdapter extends FirestoreRecyclerAdapter<CartElement, CartRecyclerAdapter.CartRecyclerViewHolder> {
    public Map<String,CartElement> Cart;
    CollectionReference CartListReference;
    DocumentReference CartDetailsReference;
    FirebaseAuth firebaseAuth;
    int TotalAmount, GrandFinalAmount;
    Context context;
    View view;
    TextView ItemPrice, DeliveryCharges,FinalAmount;
    TextView DeliveryChange, DeliveryAddress, ApplyCouponText;
    LinearLayout PlaceOrderLayout;
    ArrayList<CartElement> OrderFoodNames;


    public CartRecyclerAdapter(@NonNull FirestoreRecyclerOptions<CartElement> options , View view, Context context) {
        super(options);
        firebaseAuth = FirebaseAuth.getInstance();
        CartListReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                .document("cartlist").collection("list");
        CartDetailsReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart").document("cartdetails");
        OrderFoodNames = new ArrayList<>();
        Cart = new HashMap<>();
        this.context = context;
        this.view = view;
        ItemPrice = view.findViewById(R.id.item_total_text);
        DeliveryCharges = view.findViewById(R.id.delivery_charges_text);
        FinalAmount = view.findViewById(R.id.final_value_text);
        DeliveryChange = view.findViewById(R.id.deliver_address_operation);
        DeliveryAddress = view.findViewById(R.id.delivery_address_details);
        ApplyCouponText = view.findViewById(R.id.applycoupontext);
        PlaceOrderLayout = view.findViewById(R.id.placeorderlayout);
        CartDetailsReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()){
                            CartDetails cartDetails = task.getResult().toObject(CartDetails.class);
                            TotalAmount = cartDetails.getTotalamount();
                            String itemprice = "₹" + TotalAmount;
                            ItemPrice.setText(itemprice);
                            GrandFinalAmount = TotalAmount +30;
                            String grandtotalamount = "₹" + GrandFinalAmount;
                            FinalAmount.setText(grandtotalamount);
                        }else {
                            Toast.makeText(context, "Maa chud gyi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        DeliveryCharges.setText("₹30");

    }

    @Override
    protected void onBindViewHolder(@NonNull CartRecyclerViewHolder holder, int position, @NonNull CartElement model) {
        Cart.put(model.getFoodid(),model);
        Log.d("HASHMAP", "Added" + model.getFoodname()+ " TO LIST");
        Log.d("HASHMAP_VALUE", Cart.get(model.getFoodid()).toString());
        holder.CartElementName.setText(model.getFoodname());
        String quantity = "x" + model.getQuantity();
        holder.CartElementQuantity.setText(quantity);
        String CartValue = "₹" + model.getValue();
        holder.CartElementValue.setText(CartValue);
        String CartPrice = "₹" + model.getPrice();
        holder.CartElementPrice.setText(CartPrice);
        holder.CartElementDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCartElement(holder,model);
            }
        });

    }

    private void DeleteCartElement(@NonNull CartRecyclerViewHolder holder, @NonNull CartElement model) {

        CartDetailsReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()){
                            Cart.remove(model.getFoodid());
                            CartDetails cartDetails = task.getResult().toObject(CartDetails.class);
                            TotalAmount = cartDetails.getTotalamount();
                            TotalAmount -= model.getValue();
                            GrandFinalAmount = TotalAmount + 30;
                            cartDetails.setTotalamount(TotalAmount);
                            CartDetailsReference.set(cartDetails);
                            CartListReference.document(model.getFoodid()).delete();
                            String itemprice = "₹" + TotalAmount;
                            ItemPrice.setText(itemprice);
                            DeliveryCharges.setText("₹30");
                            GrandFinalAmount = TotalAmount +30;
                            String grandtotalamount = "₹" + GrandFinalAmount;
                            FinalAmount.setText(grandtotalamount);
                            Cart.remove(model.getFoodid());

                            if (TotalAmount==0){
                                ((Activity)context).finish();
                                CartDetailsReference.delete();
                            }
                        }
                    }
                });

    }

    public Map<String, CartElement> getCartList(){
        return Cart;
    }
    public int getGrandFinalAmount(){
        return GrandFinalAmount;
    }
    public int getTotalAmount(){
        return TotalAmount;
    }
    public String getTotalString(){
        String final_string = "";
        for (CartElement e : OrderFoodNames){
            final_string  = e.getQuantity() + "x" + e.getFoodname() + " | ";
            break;
        }
        return final_string;
    }
    @NonNull
    @Override
    public CartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_cart_item,parent,false);
        return new CartRecyclerViewHolder(view);
    }

    public class CartRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView VegTypeImageView;
        TextView CartElementName, CartElementQuantity,CartElementPrice,CartElementValue;
        Button CartElementDeleteButton;

        public CartRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            VegTypeImageView = itemView.findViewById(R.id.element_cart_vegtype);
            CartElementName = itemView.findViewById(R.id.element_cart_name);
            CartElementQuantity = itemView.findViewById(R.id.element_cart_quantity);
            CartElementPrice = itemView.findViewById(R.id.element_cart_price);
            CartElementValue = itemView.findViewById(R.id.element_cart_value);
            CartElementDeleteButton = itemView.findViewById(R.id.element_cart_delete);

        }
    }
}
