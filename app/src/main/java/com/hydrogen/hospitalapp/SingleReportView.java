package com.hydrogen.hospitalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SingleReportView extends AppCompatActivity {

    TextView testNameView, testByView,reportDescription, sampleDate, reportDate;
    SharedPref sharedDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_report_view);

        testNameView = findViewById(R.id.testNameReportView);
        testByView = findViewById(R.id.testByReportView);
        reportDescription = findViewById(R.id.reportDescriptionReportView);
        sampleDate = findViewById(R.id.sampleDateReportView);
        reportDate = findViewById(R.id.reportDateReportView);

        Intent i = getIntent();

        JsonObject enrollObject = new JsonObject();
        enrollObject.addProperty("test_for_patient_id", i.getIntExtra("testForPatientId", 0));
        enrollObject.addProperty("enroll_id", i.getIntExtra("enrollId", 0));

        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> callGetTestReport = userInterface.getTestReportDetail(sharedDatabase.getToken(),enrollObject);

        callGetTestReport.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()) {
                    JsonObject responseObject = new Gson().toJsonTree(response.body()).getAsJsonObject();
                    if (responseObject.get("status").getAsBoolean()) {

                        JsonObject reportObject = responseObject.get("data").getAsJsonObject();
                        testNameView.setText("Test Name: "+reportObject.get("test_name").getAsString());
                        testByView.setText("Test By: "+reportObject.get("emp_name").getAsString());
                        reportDescription.setText("Result: "+reportObject.get("report_description").getAsString());
                        sampleDate.setText("Sample Date: "+reportObject.get("sampled_date").getAsString().split("T")[0]);
                        reportDate.setText("Report Date: "+reportObject.get("created_date").getAsString().split("T")[0]);

                    } else {
                        testNameView.setVisibility(View.GONE);
                        testByView.setVisibility(View.GONE);
                        sampleDate.setVisibility(View.GONE);
                        reportDate.setVisibility(View.GONE);
                        reportDescription.setText(responseObject.get("message").getAsString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });



    }
}