package com.hydrogen.hospitalapp.InterfaceAPI;

import com.hydrogen.hospitalapp.Models.DonnerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DonnerInterface {

    @GET("/api/non-user/getDonner")
    Call<List<DonnerModel>> getDonner();

    @POST("/api/blood/addDonner")
    Call<DonnerModel> addDonner(@Body DonnerModel model);
}
