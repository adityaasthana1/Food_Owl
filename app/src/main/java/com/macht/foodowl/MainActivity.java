package com.macht.foodowl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation top,bottom;
    ImageView logo;
    TextView FoodOwlText;
    LinearLayout Creator;
    private static int SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        top = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash);
        bottom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.splashbottom);
        logo = findViewById(R.id.splashlogo);
        FoodOwlText = findViewById(R.id.FoodOwlText);
        Creator = findViewById(R.id.AdityaAsthana);

        logo.setAnimation(top);
        FoodOwlText.setAnimation(bottom);
        Creator.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },SPLASH_SCREEN);

    }
}