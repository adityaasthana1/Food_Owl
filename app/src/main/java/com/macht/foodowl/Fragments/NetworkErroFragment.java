package com.macht.foodowl.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.macht.foodowl.HomeActivity;
import com.macht.foodowl.R;

public class NetworkErroFragment extends Fragment {

    TextView TryAgainButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_network_error,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TryAgainButton = view.findViewById(R.id.tryagainbutton);
        TryAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext() , HomeActivity.class));
            ((Activity)getContext()).finish();
        });
    }
}

