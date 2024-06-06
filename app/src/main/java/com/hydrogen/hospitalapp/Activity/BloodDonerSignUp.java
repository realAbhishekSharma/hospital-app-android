package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.hydrogen.hospitalapp.InterfaceAPI.DonnerInterface;
import com.hydrogen.hospitalapp.Models.DonnerModel;
import com.hydrogen.hospitalapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BloodDonerSignUp extends AppCompatActivity {

    EditText phoneInput, nameInput, emailInput, districtInput, zoneInput, weightInput, pinInput;

    Spinner bloodGroupSpinner;

    String[] bloodGroupList = {"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_doner_sign_up);

        phoneInput = (EditText) findViewById(R.id.phoneInput);
        nameInput = (EditText) findViewById(R.id.nameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        districtInput = (EditText) findViewById(R.id.districtInput);
        zoneInput = (EditText) findViewById(R.id.zoneInput);
        weightInput = (EditText) findViewById(R.id.weightInput);
        pinInput = (EditText) findViewById(R.id.pinInput);

        Intent i = getIntent();
        phoneInput.setText(i.getStringExtra("Phone"));

        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bloodGroupList);
        bloodGroupSpinner.setAdapter(adapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.ApiURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DonnerInterface donnerInterface = retrofit.create(DonnerInterface.class);
        DonnerModel donnerDetail = new DonnerModel("Abhishek Sharma", "8dfeel@gmail.com", "9810478855","Morang", "Kosi", "A+","123123", 45);

        Call<DonnerModel> call = donnerInterface.addDonner(donnerDetail);
        call.enqueue(new Callback<DonnerModel>() {
            @Override
            public void onResponse(Call<DonnerModel> call, Response<DonnerModel> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(BloodDonerSignUp.this, "not Succeed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonnerModel> call, Throwable t) {

            }
        });

    }
}