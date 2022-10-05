package com.example.scream;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;


    public FetchAddressIntentService(){
        super ("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMessage ="";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location =intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if(location == null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses =null;
            try {
              addresses =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            }catch (Exception exception){
                errorMessage = exception.getMessage();
            }
            if(addresses == null || addresses.isEmpty()){
                delivarResultToReceiver(Constants.FAILURE_RESULT,errorMessage);
            }else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i=0; i<= address.getMaxAddressLineIndex(); i++ ){
                    addressFragments.add(address.getAddressLine(i));
                }
                delivarResultToReceiver(
                        Constants.SUCCESS_RESULT,
                        TextUtils.join(
                                Objects.<String>requireNonNull(System.getProperty("line.separator")),
                                addressFragments
                        )
                );
            }
        }
    }

    private void delivarResultToReceiver(int resultCode,String addressMessage){
        Bundle bundle =new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY,addressMessage);
        resultReceiver.send(resultCode, bundle);
       // String phoneNumber = "999999";
        //String myLoca = String.valueOf(addressMessage);
        //String message = "My Location = " + myLoca;
        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(phoneNumber,null,message,null,null);

    }
}
