package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    final String DATABASE = "userDatabase";
    final String TOKEN = "token";
    SharedPreferences patientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences patientData = getSharedPreferences(DATABASE, MODE_PRIVATE);

        String tempToken = patientData.getString(TOKEN, null);
        System.out.println(tempToken);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.ApiURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);


        Call<Object> callConnection = userInterface.checkServerConnection();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callConnection.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();

                        if (jsonObject.get("connection").getAsBoolean() && tempToken != null){
                            Intent i = new Intent(MainActivity.this, PatientDashboard.class);
                            startActivity(i);
                            finish();
                        }else if (jsonObject.get("connection").getAsBoolean() && tempToken == null){
                            Intent i = new Intent(MainActivity.this, Navigate.class);
                            startActivity(i);
                            finish();
                        }else if (!jsonObject.get("connection").getAsBoolean()){
                            Toast.makeText(getApplicationContext(), "Cannot connect to the server." ,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: "+t.getMessage() ,Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());
                    }
                });
            }
        }, 1000);


    }

}