package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;
import com.macht.foodowl.models.DeleteUser;

import java.util.Date;

public class DeleteAccountActivity extends AppCompatActivity {
    Button DeleteAccountButton;
    EditText DeleteAccountEmail;
    TextView DeleteMessage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    AlertDialog.Builder builder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        DeleteAccountButton = findViewById(R.id.deleteaccount_button);
        DeleteAccountEmail = findViewById(R.id.deleteaccount_email);
        DeleteMessage = findViewById(R.id.deleteaccount_message);
        
        DeleteAccountButton.setOnClickListener(v -> {
            DeleteMessage.setText("");
            String email = DeleteAccountEmail.getText().toString().trim();
            if (email.isEmpty()){
                DeleteMessage.setText("Please enter an email before proceeding");
            }else if(!email.equals(firebaseAuth.getCurrentUser().getEmail())){
                DeleteMessage.setText("Email does not match.");
            }else{
                DeleteUser deleteUser = new DeleteUser(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getEmail(), new Date().toString(), "requested");
                firebaseFirestore.collection("deleterequests")
                        .document(firebaseAuth.getCurrentUser().getUid())
                        .set(deleteUser)
                        .addOnCompleteListener(task -> {
                            DeleteMessage.setText("Your account delete request has been placed. We are sad to see you go.");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        });
            }
        });
        
    }
}