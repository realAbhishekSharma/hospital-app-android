package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.Models.UserModel;
import com.hydrogen.hospitalapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ValidationPhoneOTP extends AppCompatActivity {

    Button button;
    TextView resendOTP;
    EditText fieldPhone, fieldOTP;
    static String phoneNumber;
    String activityReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_phone_otp);

        button = (Button) findViewById(R.id.button);
        resendOTP = (TextView) findViewById(R.id.resendOTP);
        fieldPhone = (EditText) findViewById(R.id.fieldPhone);
        fieldOTP = (EditText) findViewById(R.id.fieldOTP);

        Intent intent = getIntent();
        activityReference = intent.getStringExtra("activity");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldOTP.getVisibility() == View.GONE){

                    fieldOTP.setVisibility(View.VISIBLE);
                    fieldOTP.setEnabled(true);
                    fieldPhone.setEnabled(false);

                    resendOTP.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "OTP sent.", Toast.LENGTH_LONG).show();
                    button.setText("Verify OTP");
                }else {
                    if (validateOTP(fieldOTP.getText().toString())) {
                        phoneNumber = fieldPhone.getText().toString();
                        Toast.makeText(ValidationPhoneOTP.this, "OTP validate Successfully.", Toast.LENGTH_LONG).show();

                        openActivity(activityReference);


                    }else {

                        Toast.makeText(getApplicationContext(), "OTP not verified..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ValidationPhoneOTP.this, Navigate.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });



    }

    private void openActivity(String name){

        switch(name){
            case "UserSignUp":
                startActivity(new Intent(ValidationPhoneOTP.this, UserDetailsForm.class).putExtra("activity", "UserSignUp").putExtra("phone", phoneNumber));
                finish();
                break;
            case "HospitalEnrollForm":{
                checkUser();
            }
                break;

        }
    }

    private void checkUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.ApiURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);

        JsonObject object = new JsonObject();
        object.addProperty("phone", phoneNumber);

        Call<Object> callUserByPhone = userInterface.getUserByPhone(object);

        callUserByPhone.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                JsonObject data = gson.toJsonTree(response.body()).getAsJsonObject();
                boolean userStatus = data.get("status").getAsBoolean();
                if (userStatus){
                    JsonObject userObject = data.get("user").getAsJsonObject();
                    UserModel userModel = gson.fromJson(userObject, UserModel.class);
                    System.out.println(userModel.getName());
                    Intent intent = new Intent(ValidationPhoneOTP.this, HospitalEnrollForm.class);
                    intent.putExtra("userModel", userModel);
                    intent.putExtra("activity","HospitalEnrollForm");
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(ValidationPhoneOTP.this, UserDetailsForm.class);
                    intent.putExtra("phone", phoneNumber);
                    intent.putExtra("activity","HospitalEnrollForm");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public static String getPhoneNumber(){
        return phoneNumber;

    }
    boolean validateOTP(String otp){
        if (otp.equals("123456")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Navigate.class));
        finish();
    }
}