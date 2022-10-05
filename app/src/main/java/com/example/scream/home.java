package com.example.scream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogout;
    private ImageButton log,con,mes,pol,loc,rev;
    private Intent intent;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        btnLogout = findViewById(R.id.button2);
        log=findViewById(R.id.logout1);
        con=findViewById(R.id.contact1);
        mes=findViewById(R.id.message1);
        pol=findViewById(R.id.police1);
        loc=findViewById(R.id.location1);
        rev=findViewById(R.id.review1);

//        btnLogout.setOnClickListener(this);
        log.setOnClickListener(this);
        con.setOnClickListener(this);
        mes.setOnClickListener(this);
        pol.setOnClickListener(this);
        loc.setOnClickListener(this);
        rev.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logout1) {
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(home.this,MainActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.contact1) {
            intent = new Intent(home.this,contacts.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.message1) {
            intent = new Intent(home.this,textoption.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.police1) {
            intent = new Intent(home.this,policeoption.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.review1) {
            intent = new Intent(home.this,options.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.location1) {
            intent = new Intent(home.this,location.class);
            startActivity(intent);
        }
    }
}
