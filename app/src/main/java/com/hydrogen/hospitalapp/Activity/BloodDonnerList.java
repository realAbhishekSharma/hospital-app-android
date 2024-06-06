package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.BloodDonnerListAdapter;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BloodDonnerList extends AppCompatActivity {
    RecyclerView donnerListRecyclerView;
    BloodDonnerListAdapter bloodDonnerListAdapter;

    List<JsonObject> donnerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donner_list);

        donnerListRecyclerView = findViewById(R.id.bloodDonnerRecycler);
        donnerListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donnerListRecyclerView.setHasFixedSize(true);

        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> callGetDonnerList = userInterface.getBloodDonnerList();

        callGetDonnerList.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                donnerList = new ArrayList<>();
                if (response.isSuccessful()) {
                    JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        donnerList.add(jsonArray.get(i).getAsJsonObject());
                    }

                    System.out.println(donnerList);
                    bloodDonnerListAdapter = new BloodDonnerListAdapter(donnerList, new BloodDonnerListAdapter.BloodDonnerItemClick() {
                        @Override
                        public void onItemClick(JsonObject object, int code) {
                            if (code ==0) {
                                System.out.println(object);
                                Toast.makeText(getApplicationContext(), object.get("contact").getAsString() + "\nweight: " + object.get("weight").getAsString(), Toast.LENGTH_SHORT).show();
                            }else if (code ==1){

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+object.get("contact").getAsString()));
                                startActivity(intent);
                            }
                        }
                    });

                    donnerListRecyclerView.setAdapter(bloodDonnerListAdapter);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }
}