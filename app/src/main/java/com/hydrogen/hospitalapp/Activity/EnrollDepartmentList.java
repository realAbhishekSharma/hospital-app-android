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
import com.google.gson.JsonParser;
import com.hydrogen.hospitalapp.Adapter.EnrollPreviewAdapter;
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

public class EnrollDepartmentList extends AppCompatActivity {

    RecyclerView enrollDepartmentRecyclerView;
    SharedPref sharedDatabase;

    List<JsonObject> enrollDataList;

    EnrollPreviewAdapter enrollPreviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_department_list);

        enrollDepartmentRecyclerView = (RecyclerView) findViewById(R.id.enrollDepartmentRecyclerView);
        enrollDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedDatabase = new SharedPref(this);
        Intent intent = getIntent();

        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));
        UserInterface userInterface = retrofit.create(UserInterface.class);
        JsonObject jsonObject = new JsonParser().parse(intent.getStringExtra("patientData")).getAsJsonObject();

        JsonObject patientObject = new JsonObject();
        patientObject.addProperty("patient_id", jsonObject.get("id").getAsInt());

        System.out.println(jsonObject);
        System.out.println(patientObject);
        Call<Object> callEnrollByPatient = userInterface.getEnrollByPatient(sharedDatabase.getToken(),patientObject);
        callEnrollByPatient.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                String finalValue;
                if (response.isSuccessful()){

                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                    enrollDataList = new ArrayList<>();

                    JsonArray dataArray = jsonObject.get("data").getAsJsonArray();

                    for (JsonElement element : dataArray) {
                        enrollDataList.add(element.getAsJsonObject());
                    }


                    enrollPreviewAdapter = new EnrollPreviewAdapter(getApplicationContext(),enrollDataList, new EnrollPreviewAdapter.EnrollItemClick() {
                        @Override
                        public void onItemClick(JsonObject object) {
                            System.out.println(object);
//                                        Intent i = new Intent();
//                                        i.putExtra("id", object.get("id").getAsInt());
//                                        i.putExtra("name", object.get("hospital_name").getAsString());
                        }
                    });
                    enrollDepartmentRecyclerView.setAdapter(enrollPreviewAdapter);

                    System.out.println(enrollDataList);
                }

                if (response.code() == 404){
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }
}