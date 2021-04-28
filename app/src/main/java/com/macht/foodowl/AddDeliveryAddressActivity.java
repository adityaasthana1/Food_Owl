package com.macht.foodowl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.macht.foodowl.models.DeliveryDetail;
import com.macht.foodowl.Fragments.ManualDeliveryFragment;
import com.macht.foodowl.Fragments.UnderConstructionFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddDeliveryAddressActivity extends AppCompatActivity implements LocationListener {
    LinearLayout AddManuallyLayout, CurrentLocationLayout, AddButtonLayout;
    FrameLayout frameLayout;
    EditText ManuallyAddFUllName, ManuallyAddPhoneNumber;
    DeliveryDetail NewDeliveryDetail;
    LocationManager locationManager;
    String CountryLocation;
    String StateLocation;
    String CityLocation;
    String PostalCodeLocation;
    String FullAddressLocation;
    String PinCodeLocation;
    String LandmarkLocation;
    String AreaLocation;
    String HouseNumber;
    String LongitudeGPS;
    String LatitudeGPS;
    boolean locationEnabledFlag = false;
    ImageView BackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_address);
        AddButtonLayout = findViewById(R.id.add_manually_addressbutton);
        AddManuallyLayout = findViewById(R.id.add_manually_deliveryaddress);
        CurrentLocationLayout = findViewById(R.id.add_current_deliveryaddress);
        frameLayout = findViewById(R.id.addnewaddress_framelayout);
        ManuallyAddFUllName = findViewById(R.id.add_manually_fullname);
        ManuallyAddPhoneNumber = findViewById(R.id.add_manually_mobilenumber);
        NewDeliveryDetail = null;
        BackButton = findViewById(R.id.adddeliverybackbutton);

        BackButton.setOnClickListener(v -> finish());

        AddManuallyLayout.setOnClickListener(v -> {
            AddButtonLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.addnewaddress_framelayout , new ManualDeliveryFragment()).commit();
        });

        CurrentLocationLayout.setOnClickListener(v -> {
            AddButtonLayout.setVisibility(View.INVISIBLE);
            String message = "Our GeoServices are under construction. Please add address manually. Sorry fo the inconvenience.";
            getSupportFragmentManager().beginTransaction().replace(R.id.addnewaddress_framelayout, new UnderConstructionFragment(message)).commit();

            Toast.makeText(this, "Please Add Manually.", Toast.LENGTH_SHORT).show();
        });

    }


    private void grantPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    private void isLocationEnabled() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;


        try{
            gpsEnabled =lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(AddDeliveryAddressActivity.this)
                    .setTitle("Enable GPS services please!")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        }
                    }).setNegativeButton("Cancel",null)
                    .show();
        }
    }

    private void getLocation() {
        try{
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,5,(LocationListener) this);

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try{
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationEnabledFlag = true;
            CountryLocation = addresses.get(0).getCountryName();
            StateLocation = addresses.get(0).getAdminArea();
            CityLocation = addresses.get(0).getLocality();
            PostalCodeLocation = addresses.get(0).getPostalCode();
            FullAddressLocation = addresses.get(0).getAddressLine(0);
            AreaLocation = addresses.get(0).getThoroughfare();
            HouseNumber = addresses.get(0).getSubThoroughfare();
            LandmarkLocation = addresses.get(0).getFeatureName();
            PinCodeLocation = addresses.get(0).getPostalCode();

            LongitudeGPS = Double.toString(location.getLongitude());
            LatitudeGPS = Double.toString(location.getLatitude());


        }catch (IOException e){
            Log.d("LOCATION_MANAGER", e.getMessage());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}