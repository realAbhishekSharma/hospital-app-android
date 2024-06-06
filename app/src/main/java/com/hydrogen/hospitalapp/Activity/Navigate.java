package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.SharedPref;

public class Navigate extends AppCompatActivity {
    TextView snakeBiteContact, ambulanceContact, locateHospital, bloodDonner, getEnroll, login;

    SharedPref database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);


        database = new SharedPref(this);


        String tempToken = database.getToken();
        System.out.println(tempToken);
        if (tempToken != null) {
            Intent i = new Intent(this, PatientDashboard.class);
            startActivity(i);
            finish();
        }

        snakeBiteContact = findViewById(R.id.snakeBiteContactHome);
        ambulanceContact = findViewById(R.id.ambulanceContactHome);
        locateHospital = findViewById(R.id.locateHospital);
        bloodDonner = findViewById(R.id.bloodDonner);
        getEnroll = findViewById(R.id.getEnroll);
        login = findViewById(R.id.login);

        snakeBiteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), SnakeBiteContact.class);
                startActivity(intent);
            }
        });

        ambulanceContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), ImportantContacts.class);
                startActivity(intent);
            }
        });

        locateHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), NearbyHospitals.class);
                startActivity(intent);
            }
        });



        bloodDonner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), BloodDonnerList.class);
                startActivity(intent);
            }
        });

        getEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), ValidationPhoneOTP.class);
                intent.putExtra("activity","HospitalEnrollForm");
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), PatientLogin.class);
//                intent.putExtra("ActivityReference","HospitalEnrollForm");
                startActivity(intent);
                finish();
            }
        });
    }


}