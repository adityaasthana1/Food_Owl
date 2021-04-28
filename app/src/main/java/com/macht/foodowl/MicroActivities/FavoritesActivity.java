package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.macht.foodowl.Fragments.ProfileFragment;
import com.macht.foodowl.Fragments.UnderConstructionFragment;
import com.macht.foodowl.R;

public class FavoritesActivity extends AppCompatActivity {

    FrameLayout Favorites_Framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Favorites_Framelayout = findViewById(R.id.favorites_framelayout);
       // getSupportFragmentManager().beginTransaction().replace(R.id.favorites_framelayout, new ProfileFragment()).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().replace(R.id.favorites_framelayout, new UnderConstructionFragment("This feature will be available soon! You will be able to add your favorite orders.")).commitAllowingStateLoss();

    }
}