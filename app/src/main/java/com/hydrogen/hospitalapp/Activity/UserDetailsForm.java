package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.Models.PatientModel;
import com.hydrogen.hospitalapp.Models.UserModel;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.SharedPref;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetailsForm extends AppCompatActivity {

    SharedPref sharedDatabase;
    EditText fullNameIn, emailIn, ageIn, districtIn, zoneIn, phoneIn, passwordIn, confirmPasswordIn;
    Button saveAndLogin;

    Spinner genderSpinner;


    private String gender;

    String activityDetails;

    ArrayList<String> genderList = new ArrayList<>();
    ArrayAdapter genderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_form);


        sharedDatabase = new SharedPref(this);


        fullNameIn = (EditText) findViewById(R.id.fullNameInput);
        emailIn = (EditText) findViewById(R.id.emailInput);
        ageIn = (EditText) findViewById(R.id.ageInput);
        districtIn = (EditText) findViewById(R.id.districtInput);
        zoneIn = (EditText) findViewById(R.id.zoneInput);
        phoneIn = (EditText) findViewById(R.id.phoneInput);
        passwordIn = (EditText) findViewById(R.id.passwordInput);
        confirmPasswordIn = (EditText) findViewById(R.id.confirmPasswordInput);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        saveAndLogin = (Button) findViewById(R.id.saveAndLogin);

        Intent intent = getIntent();
        activityDetails = intent.getStringExtra("activity");
        phoneIn.setText(intent.getStringExtra("phone"));


        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Other");

        genderAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,genderList);
        genderSpinner.setAdapter(genderAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.ApiURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1){
                    gender = "male";
                }else if (i == 2){
                    gender = "female";
                }else if ( i == 3){
                    gender = "others";
                }else if (i == 0){
                    gender = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveAndLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkFieldEmpty()){
                    JsonObject userObject = new JsonObject();
                    userObject.addProperty("name", fullNameIn.getText().toString().trim());
                    userObject.addProperty("username", phoneIn.getText().toString().trim());
                    userObject.addProperty("email", emailIn.getText().toString().trim());
                    userObject.addProperty("gender", gender);
                    userObject.addProperty("age", Integer.parseInt(ageIn.getText().toString()));
                    userObject.addProperty("district", districtIn.getText().toString().trim());
                    userObject.addProperty("zone", zoneIn.getText().toString().trim());
                    userObject.addProperty("password", passwordIn.getText().toString().trim());

                    if (!passwordIn.getText().toString().trim().equals(confirmPasswordIn.getText().toString().trim())){
                        Toast.makeText(getApplicationContext(), "Confirm password doesn't matched.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserModel userModel = new Gson().fromJson(userObject, UserModel.class);

                    System.out.println(userModel.getName());

                    Call<Object> callSignup = userInterface.signUpUser(userModel);

                    callSignup.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            boolean signUpStatus = new Gson().toJsonTree(response.body()).getAsJsonObject().get("status").getAsBoolean();

                            if (signUpStatus){

                                // For login user automatically
                                Call<Object> callLogin = userInterface.loginAccount(userModel);

                                callLogin.enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        String tokenValue;

                                        if (response.isSuccessful()){
                                            Gson gson = new Gson();
                                            JsonElement jsonElement = gson.toJsonTree(response.body());
                                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                                            tokenValue = "Bearer "+jsonObject.get("token").getAsString();
                                            sharedDatabase.setToken(tokenValue);

                                            switch(activityDetails){
                                                case "UserSignUp":
                                                    startActivity(new Intent(getApplicationContext(), PatientDashboard.class));
                                                    finish();
                                                    break;
                                                case "HospitalEnrollForm": {
                                                    startActivity(new Intent(getApplicationContext(), HospitalEnrollForm.class));
                                                    finish();
                                                }
                                                    break;

                                            }

                                        }else if (response.code()== 400){
                                            Toast.makeText(getApplicationContext(), "User Not Found.", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "error found", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        }


                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });


                    /*
                    Intent intent = new Intent(getApplicationContext(), EnrollPreview.class);
                    intent.putExtra("userModel", userModel);
                    startActivity(intent);*/
                }else {
                    Toast.makeText(getApplicationContext(),"Field is empty.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean checkFieldEmpty(){

        if (phoneIn.getText().toString().equals("") ||
                fullNameIn.getText().toString().equals("") ||
//                emailIn.getText().toString().equals("") ||
                ageIn.getText().toString().equals("") ||
                genderSpinner.getSelectedItemPosition() == 0 ||
                districtIn.getText().toString().equals("") ||
//                zoneIn.getText().toString().equals("") ||
                passwordIn.getText().toString().equals("") ||
                confirmPasswordIn.getText().toString().equals("")){
            return true;
        }

        return false;
    }
}