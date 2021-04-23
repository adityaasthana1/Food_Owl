package com.macht.foodowl.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.CartActivity;
import com.macht.foodowl.DeliveryActivity;
import com.macht.foodowl.LoginActivity;
import com.macht.foodowl.MicroActivities.DeveloperProfileActivity;
import com.macht.foodowl.MicroActivities.FeedBackActivity;
import com.macht.foodowl.MicroActivities.GetSupportActivity;
import com.macht.foodowl.MicroActivities.PrivacyPoliciesActivity;
import com.macht.foodowl.R;
import com.macht.foodowl.MicroActivities.SettingsActivity;
import com.macht.foodowl.models.UserDataAdapter;

public class AccountFragment extends Fragment {

    TextView AccountFullName, AccountEmail, AccountPhone;
    ImageView AccountImage;
    LinearLayout SettingsLayout,FavoriteLayout,AddressBookLayout;
    LinearLayout WriteFeedback, MyCartLayout,GetSupportLayout,PoliciesLayout,AboutLayout,DeveloperContactLayout,LogoutLayout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    AlertDialog.Builder builder;
    UserDataAdapter dataAdapter;

    public AccountFragment(){
        dataAdapter = new UserDataAdapter();
    }

    public AccountFragment(UserDataAdapter userDataAdapter){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        this.dataAdapter = userDataAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AccountFullName = view.findViewById(R.id.account_name);
        AccountEmail = view.findViewById(R.id.account_mail);
        AccountPhone = view.findViewById(R.id.account_phone);
        AccountImage = view.findViewById(R.id.account_userimage);

        SettingsLayout = view.findViewById(R.id.account_settings_layout);
        FavoriteLayout = view.findViewById(R.id.account_favorite_layout);
        AddressBookLayout = view.findViewById(R.id.account_addressbook_layout);

        WriteFeedback = view.findViewById(R.id.account_write_feedback);
        MyCartLayout = view.findViewById(R.id.account_mycart);
        GetSupportLayout = view.findViewById(R.id.account_getsupport);
        PoliciesLayout = view.findViewById(R.id.account_policieslayout);
        DeveloperContactLayout = view.findViewById(R.id.account_developer_contact);
        LogoutLayout = view.findViewById(R.id.account_logout);


        String full_name = dataAdapter.getFname() + " " + dataAdapter.lname;
        String uri = dataAdapter.getUri();
        if (!uri.isEmpty()){
            Uri ImageUri = Uri.parse(uri);
            Glide.with(getContext())
                    .load(ImageUri)
                    .into(AccountImage);
        }else {
            AccountImage.setImageResource(R.drawable.account_vector);
        }

        try{
            AccountFullName.setText(full_name);
            AccountPhone.setText(dataAdapter.getPhone());
            AccountEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        }catch (Exception e){
            Log.d("ErrorDetaisl", e.getMessage());
        }


        PoliciesLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PrivacyPoliciesActivity.class));
        });

        /*
        firebaseFirestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()){
                        UserDataAdapter dataAdapter = task.getResult().toObject(UserDataAdapter.class);
                        String full_name = dataAdapter.getFname() + " " + dataAdapter.lname;
                        String uri = dataAdapter.getUri();
                        if (!uri.isEmpty()){
                            Uri ImageUri = Uri.parse(uri);
                            Glide.with(getContext())
                                    .load(ImageUri)
                                    .into(AccountImage);
                        }else {
                            AccountImage.setImageResource(R.drawable.account_vector);
                        }


                        AccountFullName.setText(full_name);
                        AccountPhone.setText(dataAdapter.getPhone());
                        AccountEmail.setText(firebaseAuth.getCurrentUser().getEmail());
                    }
                });

         */

        GetSupportLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), GetSupportActivity.class));
        });

        WriteFeedback.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), FeedBackActivity.class));
        });

        SettingsLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        });

        MyCartLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CartActivity.class));
        });

        AddressBookLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DeliveryActivity.class));
        });

        LogoutLayout.setOnClickListener(v -> {

            builder = new AlertDialog.Builder(getContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                ((Activity)getContext()).finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {

            });
            builder.setCancelable(false);
            builder.show();
        });

        DeveloperContactLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DeveloperProfileActivity.class));
        });

    }
}
