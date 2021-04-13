package com.macht.foodowl.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.macht.foodowl.R;

public class UnderConstructionFragment extends Fragment {
    TextView CustomMessageTextView;
    String CustomMessage = "";
    public UnderConstructionFragment(){}
    public UnderConstructionFragment(String message){
        this.CustomMessage = message;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_under_construction, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomMessageTextView = view.findViewById(R.id.CustomMessage);
        CustomMessageTextView.setText(CustomMessage);
    }
}
