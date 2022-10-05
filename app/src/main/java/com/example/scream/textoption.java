package com.example.scream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class textoption extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST = 200;
    private EditText txt_message;
    private EditText txt_number;
    private EditText txt_count;
    private Button btn_manual;
    private Button btn_sms;
    private Button btn_choose;
    List<ContactResult> results=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textoption);

        txt_message = findViewById(R.id.txt_message);
        txt_number = findViewById(R.id.txt_mobile_number);
        txt_count  = findViewById(R.id.txt_count);

        btn_manual = findViewById(R.id.btn_manual);
        btn_sms = findViewById(R.id.btn_sms);
        btn_choose = findViewById(R.id.button_choose_contacts);


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();



        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MultiContactPicker.Builder(textoption.this)
                        .hideScrollbar(false)
                        .showTrack(true)
                        .searchIconColor(Color.WHITE)
                        .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
                        .handleColor(ContextCompat.getColor(textoption.this, R.color.colorPrimary))
                        .bubbleColor(ContextCompat.getColor(textoption.this, R.color.colorPrimary))
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

        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!results.isEmpty()) {
                        for(int j=0;j<results.size();j++) {
                            for (int i = 0; i < Integer.parseInt(txt_count.getText().toString()); i++) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(results.get(j).getPhoneNumbers().get(0).getNumber(),null, txt_message.getText().toString(), null, null);
                                Toast.makeText(textoption.this,"SMS Sent: count" +(i+1),Toast.LENGTH_SHORT);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendSms = new Intent(Intent.ACTION_SEND);
                sendSms.setType("text/plain");
                sendSms.putExtra(Intent.EXTRA_TEXT,txt_message.getText().toString());
                startActivity(sendSms);
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
                txt_number.setText(names);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

}

