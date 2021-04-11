package com.macht.foodowl.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.macht.foodowl.Adapters.FoodItem;
import com.macht.foodowl.Adapters.UserDataAdapter;
import com.macht.foodowl.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ProfileFragment extends Fragment {
    private UserDataAdapter userDataAdapter;
    FoodItem foodItem;
    EditText FoodName, FoodType, FoodTaste, FoodDescription, FoodGenre, FoodCategory;
    EditText FoodPrice,FoodAvailability,FoodTag;
    Button AddFoodButton, SelectImageButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView imageView;
    Uri ImageURI;
    StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    String imageUrl = " ";


    public ProfileFragment(){
        this.userDataAdapter = new UserDataAdapter();
    }
    public ProfileFragment(UserDataAdapter ua){
       this.userDataAdapter =  ua;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FoodName = view.findViewById(R.id.foodname);
        FoodType = view.findViewById(R.id.foodtype);
        FoodTaste = view.findViewById(R.id.foodtaste);
        FoodDescription = view.findViewById(R.id.fooddescription);
        FoodGenre = view.findViewById(R.id.foodgenre);
        FoodCategory = view.findViewById(R.id.foodvegtype);
        FoodPrice = view.findViewById(R.id.foodprice);
        FoodAvailability = view.findViewById(R.id.foodavailability);
        FoodTag = view.findViewById(R.id.foodtag);
        AddFoodButton = view.findViewById(R.id.foodaddbutton);
        SelectImageButton = view.findViewById(R.id.selectImageButton);
        imageView = view.findViewById(R.id.foodimagepreview);


        SelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFile();
            }
        });

        AddFoodButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Adding the food item");
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Item Progress");
            progressDialog.show();
            String foodName = FoodName.getText().toString().trim();
            String foodType = FoodType.getText().toString().trim();
            String foodTaste = FoodTaste.getText().toString().trim();
            String foodDescription = FoodDescription.getText().toString().trim();
            String foodGenre = FoodGenre.getText().toString().trim();
            String foodCategory = FoodCategory.getText().toString().trim();
            String foodPrice = FoodPrice.getText().toString().trim();
            String foodAvailability = FoodAvailability.getText().toString().trim();
            String foodTag = FoodTag.getText().toString().trim();
            String foodId = UUID.nameUUIDFromBytes(foodName.concat(foodType).getBytes()).toString();


            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();

            if(foodName.isEmpty() || foodType.isEmpty() || foodTaste.isEmpty()
            || foodDescription.isEmpty() || foodGenre.isEmpty() || foodCategory.isEmpty() || foodPrice.isEmpty()
                    || foodAvailability.isEmpty() || foodTag.isEmpty()){
                Toast.makeText(getContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
            }else{
                foodItem = new FoodItem(foodId,foodName,foodType,foodTaste,foodDescription,foodGenre,foodCategory,foodPrice,foodAvailability,foodTag,imageUrl);
                firebaseFirestore.collection("fooditems").document(foodId)
                        .set(foodItem)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Bitmap bmp = null;
                                    try {
                                        bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),ImageURI);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                                    byte[] data = baos.toByteArray();

                                    storageReference.child("foodimages").child(foodId).putBytes(data)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Log.d("IMAGE_MSG", "IMAGE SUccessfully added to firebase");
                                                    Log.d("IMG_SESSION", taskSnapshot.getUploadSessionUri().toString());
                                                }
                                            });


                                    //Intent intent = new Intent(AddResourcesActivity.this,AddImageActivity.class);
                                    //intent.putExtra("Hostel_Object",EntityID);
                                    //startActivity(intent);
                                    progressDialog.dismiss();
                                    Log.d("USER_HOSTEL","Hostel Added in User Data");
                                    Toast.makeText(getContext(), "Food Item Added.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(),"FAILED", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });

            }

        });
    }
    void SelectFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && data.getData()!=null){
            ImageURI = data.getData();
            imageView.setImageURI(ImageURI);
        }
    }
}
