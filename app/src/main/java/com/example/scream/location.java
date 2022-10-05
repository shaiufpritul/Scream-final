package com.example.scream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;

public class location extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textlatlng,textAdress;
    private ResultReceiver resultReceiver;
    private static final int CONTACT_PICKER_REQUEST = 200;
    private EditText txt_message1;
    private EditText txt_number1;
    private EditText txt_count1;
    private Button btn_manual1;
    private Button btn_sms1;
    private Button btn_choose1;
    List<ContactResult> results=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        resultReceiver = new AddressResultReceiver(new Handler());
        textlatlng = findViewById(R.id.textlatlng);
        textAdress=findViewById(R.id.textAddress);
        txt_message1 = findViewById(R.id.txt_message1);
        txt_number1 = findViewById(R.id.txt_mobile_number1);
        txt_count1  = findViewById(R.id.txt_count1);

       // btn_manual1 = findViewById(R.id.btn_manual1);
        btn_sms1 = findViewById(R.id.btn_sms1);
        btn_choose1 = findViewById(R.id.button_choose_contacts1);

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},PackageManager.PERMISSION_GRANTED);

        findViewById(R.id.buttongetcurrentlocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(location.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
            }
                else {
                    getCurrentLocation();
                }
            }
        });

        btn_choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(location.this)
                        .hideScrollbar(false)
                        .showTrack(true)
                        .searchIconColor(Color.WHITE)
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
                        .handleColor(ContextCompat.getColor(location.this, R.color.colorPrimary))
                        .bubbleColor(ContextCompat.getColor(location.this, R.color.colorPrimary))
                        .bubbleTextColor(Color.WHITE)
                        .setTitleText("Select Contacts")
                        .setLoadingType(MultiContactPicker.LOAD_ASYNC)
                        .limitToColumn(LimitColumn.NONE)
                        .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in,
                                android.R.anim.fade_out)
                        .showPickerForResult(CONTACT_PICKER_REQUEST);
            }
        });


        btn_sms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!results.isEmpty()) {
                        for(int j=0;j<results.size();j++) {
                            for (int i = 0; i < Integer.parseInt(txt_count1.getText().toString()); i++) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(results.get(j).getPhoneNumbers().get(0).getNumber(),null, txt_message1.getText().toString(), null, null);
                                Toast.makeText(location.this,"SMS Sent: count" +(i+1),Toast.LENGTH_SHORT);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                results = MultiContactPicker.obtainResult(data);
                StringBuilder names = new StringBuilder(results.get(0).getDisplayName());
                for (int j=0;j<results.size();j++){
                    if(j!=0)
                        names.append(", ").append(results.get(j).getDisplayName());
                }
                txt_number1.setText(names);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(location.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(location.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size()-1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textlatlng.setText(
                                    String.format(
                                            "Latitude : %s \n Longitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);



                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this,FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);


    }
    private class AddressResultReceiver extends ResultReceiver{
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT){
                textAdress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else {
                Toast.makeText(location.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

}