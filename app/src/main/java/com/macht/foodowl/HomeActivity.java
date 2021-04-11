package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.macht.foodowl.Adapters.UserDataAdapter;
import com.macht.foodowl.Fragments.HomeFragment;
import com.macht.foodowl.Fragments.LoadingFragment;
import com.macht.foodowl.Fragments.NetworkErroFragment;
import com.macht.foodowl.Fragments.ProfileFragment;
import com.macht.foodowl.Fragments.SearchFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ShimmerFrameLayout orderFoodShimmer;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    TextView NameText;
    UserDataAdapter userDataAdapter;

    final int WAIT_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navbar);
        frameLayout = findViewById(R.id.fragment_layout);
        NameText = findViewById(R.id.userFirstName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new LoadingFragment()).commit();

        try {
            firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userDataAdapter = documentSnapshot.toObject(UserDataAdapter.class);
                            HomeFragment homeFragment = new HomeFragment(userDataAdapter);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,homeFragment).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new NetworkErroFragment());
                        }
                    });
        }catch (Exception e){
            Log.d("Firebase_error", "Data cannot be retrievd from firebase.");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new LoadingFragment()).commit();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()){
                    case R.id.nav_dashboard:
                        temp = new HomeFragment(userDataAdapter);
                        break;
                    case R.id.nav_profile:
                        temp = new NetworkErroFragment();
                        break;
                    case  R.id.nav_search:
                        temp = new ProfileFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,temp).commit();
                return true;
            }
        });
    }
}