package com.hydrogen.hospitalapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DepartmentEnrollForm extends AppCompatActivity {


    SharedPref sharedDatabase;

    EditText appointmentDate ;
    TextView hospitalName,doctorSearch, serviceAndDepartmentSearch;

    Button datePickerIcon, proceed;

    private final int SERVICE_CODE = 1000;
    private final int DOCTOR_CODE = 3000;

    JsonObject serviceObject;
    JsonObject departmentObject;
    JsonObject doctorObject;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int departmentEnrollCharge = 599;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_enroll_form);


        Intent in = getIntent();
        JsonObject patientObject = new JsonParser().parse(in.getStringExtra("patient")).getAsJsonObject();

        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);



        hospitalName = (TextView) findViewById(R.id.hospitalNameDepartmentEnroll);
        serviceAndDepartmentSearch = (TextView) findViewById(R.id.serviceAndDepartmentSearchFromDepartment);
        doctorSearch = (TextView) findViewById(R.id.doctorSearchFromDepartment);
        appointmentDate = (EditText) findViewById(R.id.datePickerFromDepartment);

        datePickerIcon = (Button) findViewById(R.id.datePickerIconFromDepartment);
        proceed = (Button) findViewById(R.id.enrollDepartment);


        doctorSearch.setEnabled(false);
        hospitalName.setText(patientObject.get("hospital_name").getAsString());


        int monthValue = month+1;
        date = +year+"-"+monthValue+"-"+day;
        System.out.println("calender "+date);
        appointmentDate.setText(date);

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(DepartmentEnrollForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {

                        Month = 1+ Month;
                        date= Year+"-"+Month+"-"+Day;
                        appointmentDate.setText(date);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
                dialog.show();
            }
        });

        serviceAndDepartmentSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), SearchForField.class);
                i.putExtra("code", SERVICE_CODE);
                i.putExtra("hospitalId", patientObject.get("hospital_id").getAsInt());
                i.putExtra("activity", "Department");
                startActivityForResult(i, SERVICE_CODE);

            }
        });


        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), SearchForField.class);
                i.putExtra("code", DOCTOR_CODE);
                i.putExtra("hospitalId", patientObject.get("hospital_id").getAsInt());
                i.putExtra("departmentId", departmentObject.get("departmentId").getAsInt());
                i.putExtra("activity", "Department");
                startActivityForResult(i, DOCTOR_CODE);

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  modifiedDateTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault()).format(new Date());
                String admitDateTime = date+" "+ new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date());

                if (!checkFieldEmpty()){

                    JsonObject formData = new JsonObject();
                    formData.addProperty("patient_id", patientObject.get("id").getAsInt());
                    formData.addProperty("service_id", serviceObject.get("serviceId").getAsInt());
                    formData.addProperty("department_id", departmentObject.get("departmentId").getAsInt());
                    formData.addProperty("doctor_id", doctorObject.get("id").getAsInt());
                    formData.addProperty("enroll_status", 0);
                    formData.addProperty("product_name", "enroll/"+patientObject.get("title").getAsString());
                    formData.addProperty("product_amount", patientObject.get("opd_charge").getAsInt());
                    formData.addProperty("admit_date", admitDateTime);
                    formData.addProperty("modified_date", modifiedDateTime);


                    Call<Object> callGetUser = userInterface.getUserAccount(sharedDatabase.getToken());

                    callGetUser.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            JsonObject user = new Gson().toJsonTree(response.body()).getAsJsonObject().get("user").getAsJsonObject();


                            Intent intent = new Intent(DepartmentEnrollForm.this, EnrollPreview.class);
                            intent.putExtra("date", date);
                            intent.putExtra("formData", String.valueOf(formData));
                            intent.putExtra("user", String.valueOf(user));
                            intent.putExtra("serviceName", serviceObject.get("serviceName").getAsString());
                            intent.putExtra("departmentName", departmentObject.get("departmentName").getAsString());
                            intent.putExtra("hospitalName", patientObject.get("hospital_name").getAsString());
                            intent.putExtra("doctorName", doctorObject.get("name").getAsString());
                            intent.putExtra("activity", "DepartmentEnrollForm");
                            intent.putExtra("address", patientObject.get("district").getAsString());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });

                    System.out.println(formData);


                }else {
                    Toast.makeText(getApplicationContext(),"Field is empty.", Toast.LENGTH_LONG).show();
                }

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            Toast.makeText(getApplicationContext(),"Null found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == SERVICE_CODE){
            serviceObject =  new JsonObject();
            departmentObject = new JsonObject();
            serviceObject.addProperty("serviceId", data.getIntExtra("serviceId",0));
            serviceObject.addProperty("serviceName", data.getStringExtra("serviceName"));
            departmentObject.addProperty("departmentId", data.getIntExtra("depId",0));
            departmentObject.addProperty("departmentName", data.getStringExtra("departmentName"));
            serviceAndDepartmentSearch.setText(serviceObject.get("serviceName").getAsString()+"("+departmentObject.get("departmentName").getAsString()+")");

            doctorObject = null;
            doctorSearch.setText("Search Doctor");
            doctorSearch.setEnabled(true);

        }else if (requestCode == DOCTOR_CODE){
            doctorObject = new JsonObject();
            doctorObject.addProperty("id", data.getIntExtra("id", 0));
            doctorObject.addProperty("name", data.getStringExtra("name"));
            doctorSearch.setText(doctorObject.get("name").getAsString());

        }
    }

    private boolean checkFieldEmpty(){

        if (serviceObject == null ||
                departmentObject == null ||
                doctorObject == null||
                appointmentDate.getText().toString().equals("")){
            return true;
        }

        return false;
    }
}