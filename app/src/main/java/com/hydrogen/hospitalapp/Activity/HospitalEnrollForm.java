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
import com.hydrogen.hospitalapp.Fragment.DetailsTab;
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

public class HospitalEnrollForm extends AppCompatActivity {

    SharedPref sharedDatabase;

    EditText titleInput, descriptionInput,appointmentDate ;
    TextView hospitalSearch, doctorSearch, serviceAndDepartmentSearch;

    Button datePickerIcon, proceed;

    private final int SERVICE_CODE = 1000;
    private final int HOSPITAL_CODE = 2000;
    private final int DOCTOR_CODE = 3000;

    JsonObject serviceObject;
    JsonObject departmentObject;
    JsonObject doctorObject;
    JsonObject hospitalObject;

    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    String date;
    int serviceId = 0, departmentId = 0, hospitalId = 0,doctorId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_enroll_form);

        Intent in = getIntent();
//        JsonObject patientObject = new JsonParser().parse(in.getStringExtra("patient")).getAsJsonObject();

        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);




        titleInput = (EditText) findViewById(R.id.titleInput);
        descriptionInput = (EditText) findViewById(R.id.descriptionInput);
        serviceAndDepartmentSearch = (TextView) findViewById(R.id.serviceAndDepartmentSearch);
        hospitalSearch = (TextView) findViewById(R.id.hospitalSearch);
        doctorSearch = (TextView) findViewById(R.id.doctorSearch);
        appointmentDate = (EditText) findViewById(R.id.datePicker);

        datePickerIcon = (Button) findViewById(R.id.datePickerIcon);
        proceed = (Button) findViewById(R.id.proceedButton);




        hospitalSearch.setEnabled(false);
        doctorSearch.setEnabled(false);

        int monthValue = month+1;
        date = +year+"-"+monthValue+"-"+day;
        System.out.println("calender "+date);
        appointmentDate.setText(date);

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(HospitalEnrollForm.this, new DatePickerDialog.OnDateSetListener() {
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

        hospitalSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), SearchForField.class);
                i.putExtra("code", HOSPITAL_CODE);
                i.putExtra("departmentId", departmentObject.get("departmentId").getAsInt());
                i.putExtra("activity", "Hospital");
                startActivityForResult(i, HOSPITAL_CODE);

            }
        });

        serviceAndDepartmentSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), SearchForField.class);
                i.putExtra("code", SERVICE_CODE);
                i.putExtra("activity", "Hospital");
                startActivityForResult(i, SERVICE_CODE);

            }
        });


        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), SearchForField.class);
                i.putExtra("code", DOCTOR_CODE);
                i.putExtra("hospitalId", hospitalObject.get("id").getAsInt());
                i.putExtra("departmentId", departmentObject.get("departmentId").getAsInt());
                i.putExtra("activity", "Hospital");
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
                    formData.addProperty("title", titleInput.getText().toString().trim());
                    formData.addProperty("description", descriptionInput.getText().toString().trim());
                    formData.addProperty("service_id", serviceObject.get("serviceId").getAsInt());
                    formData.addProperty("department_id", departmentObject.get("departmentId").getAsInt());
                    formData.addProperty("hospital_id", hospitalObject.get("id").getAsInt());
                    formData.addProperty("doctor_id", doctorObject.get("id").getAsInt());
                    formData.addProperty("enroll_status", 0);
                    formData.addProperty("product_name", "enroll/"+titleInput.getText().toString().trim());
                    formData.addProperty("product_amount", hospitalObject.get("opd_charge").getAsInt());
                    formData.addProperty("admit_date", admitDateTime);
                    formData.addProperty("modified_date", modifiedDateTime);

                    Call<Object> callToGetUser = userInterface.getUserAccount(sharedDatabase.getToken());

                    callToGetUser.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            JsonObject user = new Gson().toJsonTree(response.body()).getAsJsonObject().get("user").getAsJsonObject();

                            System.out.println(user);
                            Intent intent = new Intent(HospitalEnrollForm.this, EnrollPreview.class);
                            intent.putExtra("date", date);
                            intent.putExtra("formData", String.valueOf(formData));
                            intent.putExtra("user", String.valueOf(user));
                            intent.putExtra("serviceName", serviceObject.get("serviceName").getAsString());
                            intent.putExtra("departmentName", departmentObject.get("departmentName").getAsString());
                            intent.putExtra("hospitalName", hospitalObject.get("name").getAsString());
                            intent.putExtra("doctorName", doctorObject.get("name").getAsString());
                            intent.putExtra("activity", "HospitalEnrollForm");
                            intent.putExtra("address", hospitalObject.get("address").getAsString());
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

            hospitalObject = null;
            doctorObject = null;
            hospitalSearch.setText("Search Hospital");
            doctorSearch.setText("Search Doctor");
            hospitalSearch.setEnabled(true);
            doctorSearch.setEnabled(false);

        }else if (requestCode == HOSPITAL_CODE){
            hospitalObject = new JsonObject();
            hospitalObject.addProperty("id", data.getIntExtra("id",0));
            hospitalObject.addProperty("name", data.getStringExtra("name"));
            hospitalObject.addProperty("opd_charge", data.getIntExtra("opd_charge",0));
            hospitalObject.addProperty("address", data.getStringExtra("address"));
            hospitalSearch.setText(hospitalObject.get("name").getAsString());

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

        if (titleInput.getText().toString().equals("") ||
//        descriptionInput.getText().toString().equals("") ||
        serviceObject == null ||
        departmentObject == null ||
        hospitalObject == null ||
        doctorObject == null||
        appointmentDate.getText().toString().equals("")){
            return true;
        }

        return false;
    }

}