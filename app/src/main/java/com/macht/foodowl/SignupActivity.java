package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.models.UserDataAdapter;

public class SignupActivity extends AppCompatActivity {
    EditText Fname,Lname,Phone,Email,Password,cPassword;
    Button SignUpButton;
    TextView TermsOfUse,PrivacyPolicy;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //setResult(RESULT_CANCELED, new Intent());
        //finish();
        Fname = findViewById(R.id.fname);
        Lname = findViewById(R.id.lname);
        Phone = findViewById(R.id.phonesignup);
        Email = findViewById(R.id.emailsignup);
        Password = findViewById(R.id.passwordsignup);
        cPassword = findViewById(R.id.confirmpasswordsu);
        SignUpButton = findViewById(R.id.signupbutton);
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Please wait while we sign you up");
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        SignUpButton.setOnClickListener(v -> {
            String firstname = Fname.getText().toString().trim();
            String lastname = Lname.getText().toString().trim();
            String emailaddress = Email.getText().toString().trim();
            String phonenumber = Phone.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String cpassword = cPassword.getText().toString().trim();


            if(firstname.isEmpty() || lastname.isEmpty() || emailaddress.isEmpty() || phonenumber.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
                Toast.makeText(this, "You cannot leave fields empty.", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(cpassword)){
                Toast.makeText(this, "Password Do not match", Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.show();
                UserDataAdapter userDataAdapter = new UserDataAdapter(firstname,lastname,emailaddress,password);
                firebaseAuth.createUserWithEmailAndPassword(emailaddress,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignupActivity.this, "Please verify your email", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(userDataAdapter)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                                progressDialog.dismiss();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, "Cant Do that!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
            }


        });



    }
}