package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    Button SigninButton;
    TextView TermsOfUse, PrivacyPolicy;
    LinearLayout SignUpButton, ForgotPasswordButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Email = findViewById(R.id.emailsignin);
        Password = findViewById(R.id.passwordsignin);
        SigninButton = findViewById(R.id.signinbutton);
        TermsOfUse = findViewById(R.id.termsofuse);
        PrivacyPolicy = findViewById(R.id.privacypolicy);
        SignUpButton = findViewById(R.id.signuptextlayout);
        ForgotPasswordButton = findViewById(R.id.forgotpasswordlayout);
        
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        
        
        SigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_LONG).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(authResult -> {
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    finish();
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                }else{
                                    Toast.makeText(LoginActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(LoginActivity.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                            });
                }
                
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this,SignupActivity.class),1);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Hello!" , Toast.LENGTH_SHORT).show();

    }
}