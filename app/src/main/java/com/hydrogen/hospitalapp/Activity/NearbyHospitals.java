package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.NearByHospitalViewAdapter;
import com.hydrogen.hospitalapp.Adapter.SearchViewAdapter;
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

public class NearbyHospitals extends AppCompatActivity {

    RecyclerView nearByHospitalRecyclerView;

    SharedPref sharedDatabase;

    List<JsonObject> dataList;
    NearByHospitalViewAdapter nearByHospitalViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospitals);

        nearByHospitalRecyclerView = findViewById(R.id.nearbyHospitalRecyclerView);
        nearByHospitalRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sharedDatabase= new SharedPref(this);


        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        if (sharedDatabase.getToken() != null) {

            Call<Object> callForHospitalList = userInterface.getAllHospitalList(sharedDatabase.getToken());

            callForHospitalList.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {

                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            dataList.add(jsonArray.get(i).getAsJsonObject());
                        }

                        System.out.println(dataList);
                        nearByHospitalViewAdapter = new NearByHospitalViewAdapter(dataList, new NearByHospitalViewAdapter.NearByHospitalClick() {
                            @Override
                            public void onItemClick(JsonObject object) {

                                System.out.println(object);
                            }
                        });

                        nearByHospitalRecyclerView.setAdapter(nearByHospitalViewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }else {
            Call<Object> callForHospitalList = userInterface.getAllHospitalListNonUser();

            callForHospitalList.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {

                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            dataList.add(jsonArray.get(i).getAsJsonObject());
                        }

                        System.out.println(dataList);
                        nearByHospitalViewAdapter = new NearByHospitalViewAdapter(dataList, new NearByHospitalViewAdapter.NearByHospitalClick() {
                            @Override
                            public void onItemClick(JsonObject object) {

                                System.out.println(object);
                            }
                        });

                        nearByHospitalRecyclerView.setAdapter(nearByHospitalViewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }

    }
}