package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hydrogen.hospitalapp.Fragment.MainDashboard;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.Models.PatientModel;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EnrollPreview extends AppCompatActivity {
    PatientModel patient;
    JsonObject bodyObject;

    SharedPref sharedDatabase;
    TextView hospitalName, hospitalAddress, date, patientName, age, gender, district, town, phone;
    TextView doctorName,services, payAmountView;

    Button saveAndPay;
    String Name, phoneNumber,password;
    int payAmount;

    String activityReference;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_preview);

        hospitalName = (TextView) findViewById(R.id.hospitalName);
        hospitalAddress = (TextView) findViewById(R.id.hospitalAddress);
        date = (TextView) findViewById(R.id.datePreview);

        patientName = (TextView) findViewById(R.id.namePreview);
        age = (TextView) findViewById(R.id.agePreview);
        gender = (TextView) findViewById(R.id.genderPreview);
        district = (TextView) findViewById(R.id.districtPreview);
        town = (TextView) findViewById(R.id.townPreview);
        phone = (TextView) findViewById(R.id.phonePreview);

        doctorName = (TextView) findViewById(R.id.doctorNamePreview);
        services = (TextView) findViewById(R.id.servicePreview);
        payAmountView = (TextView) findViewById(R.id.payAmount);

        saveAndPay = (Button) findViewById(R.id.saveAndPay);

        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);



        Intent intent = getIntent();
        activityReference = intent.getStringExtra("activity");
        date.setText(intent.getStringExtra("date"));
        JsonObject formData = new JsonParser().parse(intent.getStringExtra("formData")).getAsJsonObject();
        JsonObject userData = new JsonParser().parse(intent.getStringExtra("user")).getAsJsonObject();


        services.setText(intent.getStringExtra("serviceName") +" ("+intent.getStringExtra("departmentName")+")");
        hospitalName.setText(intent.getStringExtra("hospitalName"));
        hospitalAddress.setText(intent.getStringExtra("address"));
        doctorName.setText(intent.getStringExtra("doctorName"));


        patientName.setText(userData.get("name").getAsString());
        age.setText(userData.get("age").getAsInt()+"");
        gender.setText(userData.get("gender").getAsString());
        district.setText(userData.get("district").getAsString());
        phone.setText(userData.get("username").getAsString());
        payAmount = formData.get("product_amount").getAsInt();
        payAmountView.setText((float)payAmount/100+"");



        saveAndPay.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), activityReference, Toast.LENGTH_SHORT).show();
            if (activityReference.equals("HospitalEnrollForm")) {

                Call<Object> callEnrollToOPD = userInterface.enrollOPDbyUser(sharedDatabase.getToken(), formData);

                callEnrollToOPD.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        JsonObject responseData = new Gson().toJsonTree(response.body()).getAsJsonObject();
                        System.out.println(responseData);

                        if (response.isSuccessful()){

                            Intent in = new Intent(EnrollPreview.this, PaymentForEnroll.class);
                            in.putExtra("order_id", responseData.get("enroll_id").getAsInt());
                            in.putExtra("product_name", formData.get("product_name").getAsString());
                            in.putExtra("payAmount", payAmount);
                            startActivity(in);
                        }
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

            }else if (activityReference.equals("DepartmentEnrollForm")){
                Call<Object> callEnrollDepartment = userInterface.enrollToDepartment(sharedDatabase.getToken(),formData);

                callEnrollDepartment.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        JsonObject responseData = new Gson().toJsonTree(response.body()).getAsJsonObject();
                        System.out.println(responseData);

                        if (response.isSuccessful()){

                            Intent in = new Intent(EnrollPreview.this, PaymentForEnroll.class);
                            in.putExtra("order_id", responseData.get("enroll_id").getAsInt());
                            in.putExtra("product_name", formData.get("product_name").getAsString());
                            in.putExtra("payAmount", payAmount);
                            startActivity(in);
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

            }
        });

    }

}