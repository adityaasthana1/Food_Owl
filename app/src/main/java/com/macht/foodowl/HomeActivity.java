package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.Fragments.NoOrderFragment;
import com.macht.foodowl.models.UserDataAdapter;
import com.macht.foodowl.Fragments.AccountFragment;
import com.macht.foodowl.Fragments.EmptyCartFragment;
import com.macht.foodowl.Fragments.HomeFragment;
import com.macht.foodowl.Fragments.LoadingFragment;
import com.macht.foodowl.Fragments.NetworkErroFragment;
import com.macht.foodowl.Fragments.OrderFragment;

import java.io.IOException;

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

        if (!isNetworkConnected()){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new NetworkErroFragment()).commit();
            //bottomNavigationView.setVisibility(View.INVISIBLE);
        }else {
            try {
                firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {

                            userDataAdapter = documentSnapshot.toObject(UserDataAdapter.class);
                            HomeFragment homeFragment = new HomeFragment(userDataAdapter);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,homeFragment).commitAllowingStateLoss();

                            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                                Fragment temp = null;
                                switch (item.getItemId()){
                                    case R.id.nav_dashboard:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment(userDataAdapter)).commitAllowingStateLoss();
                                        break;
                                    case R.id.nav_profile:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new AccountFragment(userDataAdapter)).commitAllowingStateLoss();
                                        break;
                                    case R.id.nav_trackorder:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new LoadingFragment()).commitAllowingStateLoss();
                                        firebaseFirestore.collection("users")
                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                .collection("orders")
                                                .get()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful() && task.getResult().size() > 0)
                                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new OrderFragment()).commitAllowingStateLoss();
                                                    else getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new NoOrderFragment()).commitAllowingStateLoss();
                                                });
                                        break;
                                }

                                return true;
                            });
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
        }




    }

    boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}