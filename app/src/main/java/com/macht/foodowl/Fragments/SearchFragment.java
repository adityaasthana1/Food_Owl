package com.macht.foodowl.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.macht.foodowl.models.DeliveryDetail;
import com.macht.foodowl.R;

import java.util.UUID;

public class SearchFragment extends Fragment {
    EditText FullName,MobileNumber,PinCode,HouseNumber,Area,Landmark,City,State;
    Button AddAddressButton;;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FullName = view.findViewById(R.id.fullname);
        MobileNumber = view.findViewById(R.id.mobilenumber);
        PinCode = view.findViewById(R.id.pincode);
        HouseNumber = view.findViewById(R.id.housenumber);
        Area = view.findViewById(R.id.area);
        Landmark = view.findViewById(R.id.landmark);
        City = view.findViewById(R.id.city);
        State = view.findViewById(R.id.state);
        AddAddressButton = view.findViewById(R.id.addaddressbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        AddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = FullName.getText().toString();
                String mobileNumber = MobileNumber.getText().toString();
                String pinCode = PinCode.getText().toString();
                String houseNumber = HouseNumber.getText().toString();
                String area = Area.getText().toString();
                String landmark = Landmark.getText().toString();
                String city = City.getText().toString();
                String state = State.getText().toString();
                String salt = fullName+mobileNumber;
                String addressId = UUID.nameUUIDFromBytes(salt.getBytes()).toString();

                if (fullName.isEmpty() || mobileNumber.isEmpty() || pinCode.isEmpty() || houseNumber.isEmpty() || area.isEmpty()
                || landmark.isEmpty() || city.isEmpty() || state.isEmpty())
                    Toast.makeText(getContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                else{
                    String fullAddress = fullName + ", " + houseNumber + ", " + area + ", " + city + ", " + state;
                    DeliveryDetail deliveryDetail = new DeliveryDetail(addressId,fullName,mobileNumber,pinCode,houseNumber,area,landmark,city,state, fullAddress );
                    firebaseFirestore.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("deliverydetails")
                            .document(addressId)
                            .set(deliveryDetail);
                    firebaseFirestore.collection("users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("currentdetail")
                            .document("currentaddress")
                            .set(deliveryDetail);

                }
            }
        });



    }
}
