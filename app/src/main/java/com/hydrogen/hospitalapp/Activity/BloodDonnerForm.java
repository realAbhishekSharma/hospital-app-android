package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BloodDonnerForm extends AppCompatActivity {

    SharedPref sharedDatabase;
    TextView messageBox;
    EditText weightInputBox, contactInputBox, lastDonateDateBox;
    Button lastDatePicker, saveButton;

    Spinner bloodGroupSpinner;

    private String bloodGroup;
    List<String> bloodGroupList = Arrays.asList("Blood Group", "A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-");


    JsonObject donnerObject;
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donner_form);

        messageBox = (TextView) findViewById(R.id.messageBloodDonnerForm);

        weightInputBox = (EditText) findViewById(R.id.weightDonnerFrom);
        contactInputBox = (EditText) findViewById(R.id.contactDonnerForm);
        lastDonateDateBox = (EditText) findViewById(R.id.lastDonateDonnerForm);

        lastDatePicker = (Button) findViewById(R.id.lastDonateDatePickerDonnerForm);
        saveButton = (Button) findViewById(R.id.saveDonnerForm);

        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSpinnerDonnerForm);
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bloodGroupList);
        bloodGroupSpinner.setAdapter(adapter);

        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);


        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0){
                    bloodGroup = "";
                }else {
                    bloodGroup = bloodGroupList.get(i);
                }
//                Toast.makeText(getApplicationContext(), bloodGroup, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int monthValue = month+1;
        date = +year+"-"+monthValue+"-"+day;
        System.out.println("calender "+date);
        lastDonateDateBox.setText(date);

        lastDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(BloodDonnerForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int Year, int Month, int Day) {

                        Month = 1+ Month;
                        date= Year+"-"+Month+"-"+Day;
                        lastDonateDateBox.setText(date);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
                dialog.show();
            }
        });


        Call<Object> callFetchDonnerDetails = userInterface.fetchDonnerDetails(sharedDatabase.getToken());

        callFetchDonnerDetails.enqueue(new Callback<Object>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == 200){
                    JsonObject donnerResult = new Gson().toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonObject();
                    weightInputBox.setText(donnerResult.get("weight").getAsInt()+"");
                    contactInputBox.setText(donnerResult.get("contact").getAsString());
                    lastDonateDateBox.setText(donnerResult.get("last_donate").getAsString().split("T")[0]);
                    bloodGroupSpinner.setSelection(bloodGroupList.indexOf(donnerResult.get("blood_group").getAsString()));
                    System.out.println(donnerResult);

                }

                if (response.code() == 204){
                    messageBox.setText(getString(R.string.messageToBloodDonner)+"\nFill form to create account");
                    Toast.makeText(getApplicationContext(), "Blood Donner account not found.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!checkFieldEmpty()){

                    JsonObject formData = new JsonObject();
                    formData.addProperty("weight", weightInputBox.getText().toString());
                    formData.addProperty("contact", contactInputBox.getText().toString());
                    formData.addProperty("blood_group", bloodGroup);
                    formData.addProperty("last_donate", date);



                    Call<Object> saveDonnerDetails = userInterface.addDonnerDetailsForm(sharedDatabase.getToken(), formData);

                    saveDonnerDetails.enqueue(new Callback<Object>() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()){
                                String message = new Gson().toJsonTree(response.body()).getAsJsonObject().get("message").getAsString();
                                messageBox.setText(getString(R.string.messageToBloodDonner));
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });


                }else {
                    Toast.makeText(getApplicationContext(),"Field is empty.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean checkFieldEmpty(){

        if (weightInputBox.getText().toString().equals("") ||
                contactInputBox.getText().toString().equals("") ||
                bloodGroupSpinner.getSelectedItemPosition() == 0){
            return true;
        }

        return false;
    }
}