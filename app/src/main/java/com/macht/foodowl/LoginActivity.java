package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.Adapters.UserDataAdapter;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    Button SigninButton;
    TextView TermsOfUse, PrivacyPolicy;
    LinearLayout SignUpButton, ForgotPasswordButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SignInButton GooglesignInButton;
    GoogleSignInClient googleSignInClient;
    FirebaseFirestore firebaseFirestore;
    private String Tag = "LoginActivity";
    public static final int RC_SIGN_IN = 1;
    ProgressDialog progressDialog;
    GoogleSignInOptions gso;

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
        GooglesignInButton = findViewById(R.id.google_signin_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        GooglesignInButton.setOnClickListener(v -> {
            SignInWithGoogle();
        });

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

    private void SignInWithGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_SIGN_IN:
                if (data != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    HandleGoogleSignInResult(task);
                }
                break;

        }

    }

    private void HandleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {

            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Signing In");
            progressDialog.setMessage("Hold up! We are signing you up.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            firebaseAuth.signInWithCredential(authCredential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseUser = firebaseAuth.getCurrentUser();
                                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                                if (account!=null){
                                    firebaseFirestore.collection("users")
                                            .document(firebaseAuth.getCurrentUser().getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult().exists()){
                                                        //do nothing;
                                                    }else{
                                                        String FirstName = account.getGivenName();
                                                        String LastName = account.getFamilyName();
                                                        String EmailId = account.getEmail();
                                                        String userPhotoUri = account.getPhotoUrl().toString();
                                                        UserDataAdapter userDataAdapter = new UserDataAdapter(FirstName,LastName,EmailId,"none", userPhotoUri);
                                                        firebaseFirestore.collection("users")
                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                .set(userDataAdapter);
                                                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                                                        googleSignInClient.signOut();
                                                    }
                                                }
                                            });
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                }
                            }
                        }
                    });
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}