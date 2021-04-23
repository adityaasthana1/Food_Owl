package com.macht.foodowl.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.macht.foodowl.CartActivity;
import com.macht.foodowl.MicroActivities.AboutUsActivity;
import com.macht.foodowl.MicroActivities.GetSupportActivity;
import com.macht.foodowl.OrderActivity;
import com.macht.foodowl.models.UserDataAdapter;
import com.macht.foodowl.LoginActivity;
import com.macht.foodowl.OrderFoodActivity;
import com.macht.foodowl.R;
import com.synnapps.carouselview.CarouselView;

public class HomeFragment extends Fragment {
    CarouselView HomeCarousel;
    LinearLayout OrderOnlineFoodLayout, AboutUsLayout, ContactUsLayout, MyCartLayout;
    int[] OfferImages = {R.drawable.food1,R.drawable.food2,R.drawable.food3, R.drawable.food4};
    TextView UserName;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ShimmerFrameLayout orderFoodShimmer;
    UserDataAdapter userDataAdapter = null;

    public HomeFragment(){
        this.userDataAdapter = new UserDataAdapter();
    }
    public HomeFragment(UserDataAdapter ua){
        this.userDataAdapter = ua;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OrderOnlineFoodLayout = view.findViewById(R.id.orderFoodLayout);
        HomeCarousel = view.findViewById(R.id.homecarousel);
        AboutUsLayout = view.findViewById(R.id.AboutUsLayout);
        ContactUsLayout = view.findViewById(R.id.contactus);
        MyCartLayout = view.findViewById(R.id.MyCartLayout);

        UserName = view.findViewById(R.id.userFirstName);
        firebaseAuth = FirebaseAuth.getInstance();

        if (userDataAdapter!=null) {
            UserName.setText(userDataAdapter.getFname());
            HomeCarousel.setImageListener((position, imageView) -> imageView.setImageResource(OfferImages[position]));
        }
        OrderOnlineFoodLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OrderFoodActivity.class));
        });

        ContactUsLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), GetSupportActivity.class));

        });

        AboutUsLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AboutUsActivity.class));
        });

        MyCartLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CartActivity.class));
        });

    }
}
