package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.PatientViewAdapter;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EnrollPatientHistory extends AppCompatActivity {

    RecyclerView enrollPatientHistory;
    PatientViewAdapter patientViewAdapter;
    SharedPref sharedDatabase;
    List<JsonObject> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_patient_history);
        setTitle("Patient History");

        sharedDatabase = new SharedPref(this);

        enrollPatientHistory = (RecyclerView) findViewById(R.id.enrollPatientHistory);
        enrollPatientHistory.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> callPatientHistory = userInterface.getAllPatientHistory(sharedDatabase.getToken());

        callPatientHistory.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                patientList = new ArrayList<>();
                JsonArray dataArray = new Gson().toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                for (JsonElement element: dataArray){
                    patientList.add(element.getAsJsonObject());
                }

                patientViewAdapter = new PatientViewAdapter(getApplicationContext(),patientList, new PatientViewAdapter.PatientItemClick() {
                    @Override
                    public void onItemClick(JsonObject object) {
                        Intent intent = new Intent(EnrollPatientHistory.this, EnrollDepartmentList.class);
                        intent.putExtra("patientData", String.valueOf(object));
                        System.out.println(object);
                        startActivity(intent);
                    }
                });
                enrollPatientHistory.setAdapter(patientViewAdapter);


                System.out.println(patientList);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }
}