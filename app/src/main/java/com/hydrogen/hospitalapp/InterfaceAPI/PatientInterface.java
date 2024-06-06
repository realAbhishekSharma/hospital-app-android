package com.hydrogen.hospitalapp.InterfaceAPI;

import com.hydrogen.hospitalapp.Models.DonnerModel;
import com.hydrogen.hospitalapp.Models.PatientLoginModel;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface PatientInterface {

    @POST("/api/patient/login")
    Call<PatientLoginModel> loginPatient(@Body PatientLoginModel object);

    @Headers({"Token: application/json"})
    @GET("/api/patient/reports")
    Call<DonnerModel> getReport();

    @GET("/api/patient/enroll-by-patient")
    Call<List<Object>> getEnrollByPatient(@Header("Authorization") String token , @Body String patient_id);

}
