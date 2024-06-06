package com.hydrogen.hospitalapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SharedPref extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final String USER_PREF = "Users";
    private final String TOKEN = "token";


    public SharedPref(Context context){
        this.sharedPreferences = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
    }

    public String getToken(){
        return sharedPreferences.getString(TOKEN, null);
    }

    public void setToken(String value){
        sharedPreferences.edit().putString(TOKEN, value).apply();
    }

    public void deleteToken(){
        sharedPreferences.edit().clear().apply();
    }


}
