package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.PatientInterface;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.Models.UserModel;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientLogin extends AppCompatActivity {

    final String TOKEN = "token";
    SharedPref sharedDatabase;


    EditText usernameIn, passwordIn;
    Button login;
    TextView forgetPassword, signUpButton;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        sharedDatabase = new SharedPref(this);

        String tempToken = sharedDatabase.getToken();
        System.out.println(tempToken);
        if (tempToken != null) {
            Intent i = new Intent(PatientLogin.this, PatientDashboard.class);
            startActivity(i);
            finish();
        }

        usernameIn = (EditText) findViewById(R.id.usernameIn);
        passwordIn = (EditText) findViewById(R.id.passwordIn);
        login = (Button) findViewById(R.id.login);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        signUpButton = (TextView) findViewById(R.id.signUp);

//        Retrofit retrofit = RetrofitInstance.getInstance(getString(R.string.ApiURL), tempToken);
        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));


        UserInterface userInterface = retrofit.create(UserInterface.class);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( getApplicationContext(), ValidationPhoneOTP.class);
                intent.putExtra("activity", "UserSignUp");
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                UserModel userLogin = new UserModel(usernameIn.getText().toString().trim(), passwordIn.getText().toString().trim());
                Call<Object> call = userInterface.loginAccount(userLogin);

//                forgetPassword.setText(userLogin.getUsername()+" "+userLogin.getPassword());

                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        String tokenValue;

                        if (response.isSuccessful()){
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(response.body());
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            tokenValue = "Bearer "+jsonObject.get("token").getAsString();
                            forgetPassword.setText(tokenValue);
//                            System.out.println(jsonObject.get("token"));
                            sharedDatabase.setToken(tokenValue);

                            Intent i = new Intent(PatientLogin.this, PatientDashboard.class);
                            startActivity(i);
                            finish();
                        }else if (response.code()== 400){
                            Toast.makeText(PatientLogin.this, "User Not Found.", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(PatientLogin.this, "error found", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getApplicationContext(), Navigate.class);
        startActivity(intent);
        finish();
    }
}