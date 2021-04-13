package com.macht.foodowl.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.Adapters.DeliveryDetail;
import com.macht.foodowl.R;

import java.util.UUID;

public class ManualDeliveryFragment extends Fragment {
    EditText Name,PhoneNumber, PinCode,HouseNumber,Area,Landmark,City,State;
    LinearLayout AddAddressLayout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public ManualDeliveryFragment(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual_delivery,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Name = getActivity().findViewById(R.id.add_manually_fullname);
        PhoneNumber = getActivity().findViewById(R.id.add_manually_mobilenumber);
        PinCode = view.findViewById(R.id.add_manually_pincode);
        HouseNumber = view.findViewById(R.id.add_manually_housenumber);
        Area = view.findViewById(R.id.add_manually_area);
        Landmark = view.findViewById(R.id.add_manually_landmark);
        City = view.findViewById(R.id.add_manually_city);
        State = view.findViewById(R.id.add_manually_state);
        AddAddressLayout = getActivity().findViewById(R.id.add_manually_addressbutton);

        AddAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AddManuallyName = Name.getText().toString().trim();
                String AddManuallyPhoneNumber = PhoneNumber.getText().toString().trim();
                String AddManuallyPincode = PinCode.getText().toString().trim();
                String AddManuallyHouseNumber = HouseNumber.getText().toString().trim();
                String AddManuallyArea = Area.getText().toString().trim();
                String AddManuallyLandmark = Landmark.getText().toString().trim();
                String AddManuallyCity = City.getText().toString().trim();
                String AddManuallyState = State.getText().toString().trim();
                String salt = AddManuallyName + AddManuallyPhoneNumber;
                String AddManuallyId = UUID.nameUUIDFromBytes(salt.getBytes()).toString();
                String FullAddress = AddManuallyName + ", " + AddManuallyHouseNumber + ", " + AddManuallyArea + ", " + AddManuallyCity + ", " + AddManuallyPincode;

                if (AddManuallyName.isEmpty() || AddManuallyPhoneNumber.isEmpty() || AddManuallyHouseNumber.isEmpty() || AddManuallyArea.isEmpty()
                || AddManuallyLandmark.isEmpty() || AddManuallyCity.isEmpty() || AddManuallyState.isEmpty()){
                    Toast.makeText(getContext(), "You Cannot Leave Fields Empty", Toast.LENGTH_SHORT).show();
                }else{
                    DeliveryDetail newDeilveryDetail = new DeliveryDetail(AddManuallyId,AddManuallyName,AddManuallyPhoneNumber,
                            AddManuallyPincode,AddManuallyHouseNumber,AddManuallyArea,AddManuallyLandmark,
                            AddManuallyCity,AddManuallyState, FullAddress);
                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid())
                            .collection("deliverydetails")
                            .document(AddManuallyId)
                            .set(newDeilveryDetail);
                    firebaseFirestore.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("currentdetail")
                            .document("currentaddress")
                            .set(newDeilveryDetail);
                    Intent intent = new Intent();
                    intent.putExtra("ADDED_DELIVERY_ADDRESS" , newDeilveryDetail);
                    ((Activity)getContext()).setResult(Activity.RESULT_OK , intent);
                    ((Activity)getContext()).finish();
                }

            }
        });


    }
}
