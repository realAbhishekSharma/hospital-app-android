package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.SearchViewAdapter;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchForField extends AppCompatActivity {

    SharedPref sharedDatabase;

    private final int SERVICE_CODE = 1000;
    private final int HOSPITAL_CODE = 2000;
    private final int DOCTOR_CODE = 3000;

    int requestCode;

    EditText searchBox;
    TextView cancelButton;
    RecyclerView searchListRecyclerView;

    List<JsonObject> dataList;

    SearchViewAdapter searchViewAdapter;

    String activityReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_field);

        sharedDatabase= new SharedPref(this);
        Intent intent = getIntent();
        requestCode = intent.getIntExtra("code", 1);
        activityReference = intent.getStringExtra("activity");



        searchBox = (EditText) findViewById(R.id.searchFieldBox);
        cancelButton = (TextView) findViewById(R.id.cancelButton);
        searchListRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        searchListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        searchListRecyclerView.setHasFixedSize(true);

//        Retrofit retrofit = RetrofitInstance.getInstance(getString(R.string.ApiURL), token);


        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        JsonObject depObj = new JsonObject();
        depObj.addProperty("department_id", intent.getIntExtra("departmentId",0));
        Call<Object> callGetHospitals = userInterface.getHospitalByDepartment(sharedDatabase.getToken(),depObj);

        Call<Object> callGetService;

        if (activityReference.equals("Department")){
            JsonObject hospitalId = new JsonObject();
            hospitalId.addProperty("hospital_id", intent.getIntExtra("hospitalId",0));
            System.out.println("from Search act "+hospitalId);
            callGetService = userInterface.getServiceDepartmentByHospital(sharedDatabase.getToken(),hospitalId);

        }else {
            callGetService = userInterface.getServices(sharedDatabase.getToken());
        }

        JsonObject hospDepObj = new JsonObject();
        hospDepObj.addProperty("hospital_id",intent.getIntExtra("hospitalId",0));
        hospDepObj.addProperty("department_id",intent.getIntExtra("departmentId",0));
        Call<Object> callGetDoctors = userInterface.getDoctorsByHospitalDepartment(sharedDatabase.getToken(),hospDepObj);


        if (requestCode == HOSPITAL_CODE) {

            callGetHospitals.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {

                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            dataList.add(jsonArray.get(i).getAsJsonObject());
                        }

                        searchViewAdapter = new SearchViewAdapter(dataList, new SearchViewAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(JsonObject object) {

                                Intent i = new Intent();
                                System.out.println("hospital data"+object);
                                i.putExtra("id", object.get("id").getAsInt());
                                i.putExtra("name", object.get("hospital_name").getAsString());
                                i.putExtra("opd_charge", object.get("opd_charge").getAsInt());
                                i.putExtra("address", object.get("district").getAsString());
                                setResult(requestCode, i);
                                finish();
                            }
                        });
                        searchListRecyclerView.setAdapter(searchViewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }else if (requestCode == SERVICE_CODE){

            callGetService.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {

                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            dataList.add(jsonArray.get(i).getAsJsonObject());
                        }

                        searchViewAdapter = new SearchViewAdapter(dataList, new SearchViewAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(JsonObject object) {
                                Intent i = new Intent();
                                i.putExtra("serviceId", object.get("service_id").getAsInt());
                                i.putExtra("serviceName", object.get("service_name").getAsString());
                                i.putExtra("depId", object.get("dep_id").getAsInt());
                                i.putExtra("departmentName", object.get("department_name").getAsString());
                                setResult(requestCode, i);
                                finish();
                            }
                        });
                        searchListRecyclerView.setAdapter(searchViewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });

        }else if (requestCode == DOCTOR_CODE){

            callGetDoctors.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Gson gson = new Gson();
                    dataList = new ArrayList<>();
                    if (response.isSuccessful()) {

                        JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            dataList.add(jsonArray.get(i).getAsJsonObject());
                        }

                        searchViewAdapter = new SearchViewAdapter(dataList, new SearchViewAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(JsonObject object) {
                                Intent i = new Intent();
                                i.putExtra("id", object.get("id").getAsInt());
                                i.putExtra("name", object.get("doctor_name").getAsString());
                                setResult(requestCode, i);
                                finish();
                            }
                        });
                        searchListRecyclerView.setAdapter(searchViewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });

        }



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}