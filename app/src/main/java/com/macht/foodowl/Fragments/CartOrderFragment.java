package com.macht.foodowl.Fragments;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.macht.foodowl.Adapters.CartDetails;
import com.macht.foodowl.Adapters.CartElement;
import com.macht.foodowl.Adapters.CartRecyclerAdapter;
import com.macht.foodowl.Adapters.DeliveryDetail;
import com.macht.foodowl.Adapters.FoodItem;
import com.macht.foodowl.Adapters.FoodRecyclerAdapter;
import com.macht.foodowl.Adapters.OrderAdapter;
import com.macht.foodowl.Adapters.UserOrder;
import com.macht.foodowl.DeliveryActivity;
import com.macht.foodowl.PaymentActivity;
import com.macht.foodowl.R;
import com.macht.foodowl.TrackOrderActivity;
import com.razorpay.Checkout;
import com.razorpay.OTP;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CartOrderFragment extends Fragment{
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    CartRecyclerAdapter cartRecyclerAdapter;
    CollectionReference CartListReference;
    DocumentReference CartDetailsReference;
    FirebaseAuth firebaseAuth;
    LinearLayout PlaceOrderLayout;
    Map<String, CartElement> FinalCart;
    int ItemTotal,GrandFinalAmount, deliveryAmount;
    TextView DeliveryChange, DeliveryAddress, ApplyCouponText;
    DeliveryDetail CurrentDelivery = null;
    public static final int DELIVERY_ADDRESS_OPERATION = 1;
    public static final int PAYMENT_OPERATION = 2;
    ProgressDialog progressDialog;
    String Final_Food_String;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        DeliveryChange = view.findViewById(R.id.deliver_address_operation);
        DeliveryAddress = view.findViewById(R.id.delivery_address_details);
        ApplyCouponText = view.findViewById(R.id.applycoupontext);
        ApplyCouponText.setOnClickListener(v -> {
            Toast.makeText(getContext(), "No Coupons Available for now.", Toast.LENGTH_SHORT).show();
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        CartListReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart")
                .document("cartlist").collection("list");
        CartDetailsReference = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("cart").document("cartdetails");
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("currentdetail")
               .document("currentaddress")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DeliveryDetail deliveryDetail = task.getResult().toObject(DeliveryDetail.class);
                        DeliveryChange.setText(R.string.change);
                        String address = deliveryDetail.getFulladdress();
                        DeliveryAddress.setText(address);
                        CurrentDelivery = deliveryDetail;
                    }
                    else{
                        DeliveryChange.setText(R.string.add);
                    }
                });

        PlaceOrderLayout = view.findViewById(R.id.placeorderlayout);
        SetUpCartRecycletView(view);
        DeliveryChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), DeliveryActivity.class), 1);
            }
        });

        PlaceOrderLayout.setOnClickListener(v -> {
            deliveryAmount = 30;
            GrandFinalAmount = cartRecyclerAdapter.getGrandFinalAmount();
            ItemTotal = cartRecyclerAdapter.getTotalAmount();
            FinalCart = cartRecyclerAdapter.getCartList();
            Final_Food_String = cartRecyclerAdapter.getTotalString();

            Map<String, CartElement> CartFinal = FinalCart;
            firebaseFirestore.collection("random")
                    .document("list")
                    .set(CartFinal);


            if (CurrentDelivery == null){
                Toast.makeText(getContext(), "Please Add Delivery Address.", Toast.LENGTH_LONG).show();
                return;
            }
            if (GrandFinalAmount == 0){
                Toast.makeText(getContext(), "Grand Total 0", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Placing Order");
            progressDialog.setMessage("Please wait while we are processing your order.");
            progressDialog.show();
            Intent intent = new Intent(getContext(),PaymentActivity.class);
            String full_address = CurrentDelivery.getFullname() + ", " + CurrentDelivery.getHousenumber() + ", " + CurrentDelivery.getArea() + ", " + CurrentDelivery.getCity() + ", " + CurrentDelivery.getState();
            CurrentDelivery.setFulladdress(full_address);
            OrderAdapter orderAdapter= new OrderAdapter(null,firebaseAuth.getCurrentUser().getUid(),
                    new Date().toString(),"active","placed",
                    GrandFinalAmount,ItemTotal,deliveryAmount,
                    0,CurrentDelivery,new CartDetails(GrandFinalAmount,new Date().toString())
                    ,Final_Food_String,CartFinal);
            orderAdapter.setCart_list(CartFinal);
            intent.putExtra("FINAL_PAYMENT", GrandFinalAmount);
            intent.putExtra("ORDER_OBJECT", orderAdapter);
            startActivityForResult(intent, PAYMENT_OPERATION);
        });

    }

    private void SetUpCartRecycletView(@NonNull View view) {
        Query query = CartListReference.orderBy("quantity", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CartElement> options = new FirestoreRecyclerOptions.Builder<CartElement>()
                .setQuery(query, CartElement.class)
                .build();
        cartRecyclerAdapter = new CartRecyclerAdapter(options,view,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartRecyclerAdapter);
        Log.d("ADAPTER","CART RECYCLER ADAPTER Created SUccessfully");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case DELIVERY_ADDRESS_OPERATION:
                if (data != null) {
                    Toast.makeText(getContext(), "We are here", Toast.LENGTH_SHORT).show();
                    CurrentDelivery = data.getParcelableExtra("NEW_DELIVERY_ADDRESS");
                    Log.d("CURRENT_DELIVERY", CurrentDelivery.getAddressid() + " " + CurrentDelivery.getHousenumber() + " " + CurrentDelivery.getArea() + " " + CurrentDelivery.getCity());
                    DeliveryChange.setText(R.string.change);
                    String address = CurrentDelivery.getFullname() + ", " + CurrentDelivery.getHousenumber() + ", " + CurrentDelivery.getArea() + ", " + CurrentDelivery.getCity() + ", " + CurrentDelivery.getState();
                    CurrentDelivery.setFulladdress(address);
                    DeliveryAddress.setText(address);
                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                            .collection("currentdetail")
                            .document("currentaddress")
                            .set(CurrentDelivery);

                }else Toast.makeText(getContext() , "Nothing was returned.", Toast.LENGTH_SHORT).show();
                break;
            case PAYMENT_OPERATION:
                if (data!=null){

                    String confirmation =data.getStringExtra("CONFIRMATION");
                    String message = data.getStringExtra("MESSAGE");
                    String order_id =  data.getStringExtra("ORDER_ID");
                    OrderAdapter orderAdapter = data.getParcelableExtra("ORDER");
                    final String[] complete_string = {""};
                    if (confirmation.equals("TRUE")){
                        //Place Order
                        progressDialog.dismiss();
                        Intent intent = new Intent(getContext(),TrackOrderActivity.class);
                        intent.putExtra("ORDER", orderAdapter);
                        CartListReference.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){

                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                CartElement element = document.toObject(CartElement.class);
                                                complete_string[0] = element.getQuantity() + "x" + element.getFoodname() + " | ";
                                                firebaseFirestore.collection("orders")
                                                        .document(order_id)
                                                        .collection("cartlist")
                                                        .document(element.getFoodid())
                                                        .set(element);

                                                String key = document.getString("foodid");
                                                CartListReference.document(key)
                                                        .delete();
                                            }
                                        }

                                    }
                                });
                       CartDetailsReference.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });



                    }else if (confirmation.equals("FALSE")){
                        //PaymentFailed
                        Toast.makeText(getContext() , message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }//DoNothing

                }


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cartRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cartRecyclerAdapter.stopListening();
    }

}


