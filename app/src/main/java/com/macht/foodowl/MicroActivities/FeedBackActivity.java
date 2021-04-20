package com.macht.foodowl.MicroActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.R;

import java.util.Date;
import java.util.HashMap;

public class FeedBackActivity extends AppCompatActivity {
    EditText feedback;
    Button feedbackbutton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        feedback = findViewById(R.id.feedback_edittext);
        feedbackbutton = findViewById(R.id.feedbackbutton);

        feedbackbutton.setOnClickListener(v -> {
            String review = feedback.getText().toString().trim();
            HashMap<String,String> map = new HashMap<>();

            if (review.isEmpty()){
                Toast.makeText(this, "You can't submit empty review :D", Toast.LENGTH_SHORT).show();
            }
            map.put("feedback", review);
            map.put("time" , new Date().toString());
            map.put("user" , firebaseAuth.getCurrentUser().getUid());

            firebaseFirestore.collection("feedbacks")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .set(map)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Feedback Submitted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(this, "There was some error.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}