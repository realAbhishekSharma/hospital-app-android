package com.hydrogen.hospitalapp.InterfaceAPI;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserInterface  {

    @GET("/api/non-user/server-connection")
    Call<Object> checkServerConnection();

    @GET("/api/non-user/get-donner-list")
    Call<Object> getBloodDonnerList();


    @POST("/api/main-user/add-donner-details")
    Call<Object> addDonnerDetailsForm(@Header("Authorization") String token, @Body JsonObject object);


    @GET("/api/main-user/fetch-donner-details")
    Call<Object> fetchDonnerDetails(@Header("Authorization") String token);


    @POST("/api/non-user/get-user-by-phone")
    Call<Object> getUserByPhone(@Body Object phone);


    @GET("/api/non-user/get-all_hospitals")
    Call<Object> getAllHospitalListNonUser();

    @POST("/api/non-user/sign-up")
    Call<Object> signUpUser(@Body UserModel userModel);


    @GET("/api/main-user/fetch-user-details")
    Call<Object> getUserAccount(@Header("Authorization") String token);

    @POST("/api/main-user/login")
    Call<Object> loginAccount(@Body UserModel userModel);

    @Headers("Content-Type: application/json")
    @POST("/api/patient/get-enroll-by-patient")
    Call<Object> getEnrollByPatient(@Header("Authorization") String token, @Body() JsonObject object);

    @Headers("Content-Type: application/json")
    @GET("/api/patient/get-active-patient-by-user")
    Call<Object> getActivePatientByUser(@Header("Authorization") String token);


    @GET("/api/main-user/get-service-department-list")
    Call<Object> getServices(@Header("Authorization") String token);


    @GET("/api/main-user/get-hospital-list")
    Call<Object> getHospitals(@Header("Authorization") String token);


    @GET("/api/main-user/get-doctor-list")
    Call<Object> getDoctors(@Header("Authorization") String token);


    @POST("/api/main-user/patient-admit-opd")
    Call<Object> enrollOPDbyUser(@Header("Authorization") String token, @Body JsonObject object);


    @POST("/api/patient/enroll-department")
    Call<Object> enrollToDepartment(@Header("Authorization") String token, @Body JsonObject object);


    @POST("/api/patient/enroll-department")
    Call<Object> enrollDepartmentByPatient(@Header("Authorization") String token, @Body JsonObject object);


    @POST("/api/main-user/get-service-department-by-hospital")
    Call<Object> getServiceDepartmentByHospital(@Header("Authorization") String token, @Body JsonObject object);

    @POST("/api/main-user/get-doctor-by-department-hospital")
    Call<Object> getDoctorsByHospitalDepartment(@Header("Authorization") String token, @Body JsonObject object);

    @POST("/api/main-user/get-hospital-by-department")
    Call<Object> getHospitalByDepartment(@Header("Authorization") String token, @Body JsonObject object);


    @GET("/api/main-user/get-all-patient")
    Call<Object> getAllPatientHistory(@Header("Authorization") String token);


    @GET("/api/main-user/get-all-hospital-list")
    Call<Object> getAllHospitalList(@Header("Authorization") String token);


    @POST("/api/main-user/save-payment-by-order-id")
    Call<Object> savePaymentByOrderId(@Header("Authorization") String token, @Body JsonObject object);

    @POST("/api/main-user/check-payment-status-by-enroll")
    Call<Object> checkPaymentStatusByEnroll(@Header("Authorization") String token, @Body JsonObject object);


    @GET("/api/non-user/get-snake-bite-contacts")
    Call<Object> getSnakeBiteContactList(@Header("Authorization") String token);


    @POST("/api/patient/get-tests-by-enroll")
    Call<Object> getAllTestByEnroll(@Header("Authorization") String token, @Body JsonObject object);

    @POST("/api/patient/get-report-by-enroll-and-tfp")
    Call<Object> getTestReportDetail(@Header("Authorization") String token, @Body JsonObject object);


    @POST("/api/patient/send-enroll-details-to-employee")
    Call<Object> sendEnrollDataToEmployee(@Header("Authorization") String token, @Body JsonObject object);

}
