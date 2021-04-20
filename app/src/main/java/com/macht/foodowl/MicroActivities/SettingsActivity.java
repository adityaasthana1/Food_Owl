package com.macht.foodowl.MicroActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.macht.foodowl.DeliveryActivity;
import com.macht.foodowl.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    LinearLayout EditInformationLayout, ChangePasswordLayout, UploadImageLayout,DeleteAccountLayout;
    ImageView BackButton;
    Uri ImageURI;
    public static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        EditInformationLayout = findViewById(R.id.account_settings_editdetails);
        ChangePasswordLayout = findViewById(R.id.account_settings_changepassword);
        DeleteAccountLayout = findViewById(R.id.account_settings_deleteaccount);
        BackButton = findViewById(R.id.account_settings_backbutton);

        ChangePasswordLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        });

        EditInformationLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), EditInformationActivity.class));
        });


        BackButton.setOnClickListener(v -> {
            finish();
        });

    }

}