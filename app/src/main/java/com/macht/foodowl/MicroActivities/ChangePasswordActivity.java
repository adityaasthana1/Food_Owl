package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.macht.foodowl.R;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText EmailInput;
    Button SendPasswordButton;
    TextView textView;
    FirebaseAuth firebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        EmailInput = findViewById(R.id.changepassword_edittext);
        SendPasswordButton = findViewById(R.id.changepassword_button);
        textView = findViewById(R.id.error_message);
        firebaseAuth = FirebaseAuth.getInstance();
        
        SendPasswordButton.setOnClickListener(v -> {
            String email = EmailInput.getText().toString().trim();
            if (email.isEmpty()){
                textView.setText("Please enter your Email.");
                return;
            }
            
            if (!email.equals(firebaseAuth.getCurrentUser().getEmail())){
                textView.setText("This is not your registered Email.");
                return;
            }
            
            firebaseAuth.sendPasswordResetEmail(email);
            Toast.makeText(this, "Password Reset Email Sent.", Toast.LENGTH_SHORT).show();
            
        });
    }
}