package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;
import com.macht.foodowl.models.UserDataAdapter;

import java.util.HashMap;

public class EditInformationActivity extends AppCompatActivity {
    EditText FirstName, LastName, Phone;
    Button EditInformationButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    UserDataAdapter userDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        FirstName = findViewById(R.id.editinformation_firstname);
        LastName = findViewById(R.id.editinformation_lastname);
        Phone = findViewById(R.id.editinformation_phone);
        EditInformationButton = findViewById(R.id.editinformation_button);
        progressDialog = new ProgressDialog(EditInformationActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Please wait while we fetch ytour data");
        progressDialog.show();


        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()){
                        userDataAdapter = task.getResult().toObject(UserDataAdapter.class);
                        FirstName.setText(userDataAdapter.getFname());
                        LastName.setText(userDataAdapter.getLname());
                        Phone.setText(userDataAdapter.getPhone());
                        progressDialog.dismiss();

                    }
                });

        EditInformationButton.setOnClickListener(v -> {
            progressDialog.show();
            String Fname = FirstName.getText().toString().trim();
            String Lname = LastName.getText().toString().trim();
            String phone = Phone.getText().toString().trim();
            if (Fname.isEmpty() || Lname.isEmpty() || phone.isEmpty()){
                Toast.makeText(this, "Empty Fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            userDataAdapter.setFname(Fname);
            userDataAdapter.setLname(Lname);
            userDataAdapter.setPhone(phone);

            UserDataAdapter userdata = new UserDataAdapter(Fname,Lname,firebaseAuth.getCurrentUser().getEmail(),phone, userDataAdapter.getUri());

            firebaseFirestore.collection("users")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .set(userdata)
                    .addOnCompleteListener(task1 -> {
                        Toast.makeText(this, "Your data is updated", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    });

        });




    }
}