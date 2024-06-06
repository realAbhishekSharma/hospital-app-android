package com.hydrogen.hospitalapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.About;
import com.hydrogen.hospitalapp.Activity.BloodDonnerForm;
import com.hydrogen.hospitalapp.Activity.EnrollPatientHistory;
import com.hydrogen.hospitalapp.Activity.HospitalEnrollForm;
import com.hydrogen.hospitalapp.Activity.Navigate;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    SharedPref database;

    ImageView userImageView;
    TextView userNameView, userPhoneView, userAddressView, getLiveLocation;
    TextView accountDetails, myAccount;
    TextView bloodDonnerAccount, admitToHospital, patientHistory, reportsHistory;
    TextView setting, about, logoutButton;

    JsonObject userData;

    FusedLocationProviderClient client;
    private static int REQUEST_CODE= 100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        database = new SharedPref(view.getContext());

        userNameView = (TextView) view.findViewById(R.id.userNameView);
        userPhoneView = (TextView) view.findViewById(R.id.userPhoneView);
        userAddressView = view.findViewById(R.id.userAddressViewInAccount);
        getLiveLocation = view.findViewById(R.id.getLiveLocationInAccount);

        client = LocationServices.getFusedLocationProviderClient(getContext());

        accountDetails = (TextView) view.findViewById(R.id.accountDetails);
        myAccount = (TextView) view.findViewById(R.id.myAccount);

        bloodDonnerAccount = (TextView) view.findViewById(R.id.bloodDonnerAccount);
        admitToHospital = (TextView) view.findViewById(R.id.admitToHospital);
        patientHistory = (TextView) view.findViewById(R.id.patientHistory);
        reportsHistory = (TextView) view.findViewById(R.id.reportsHistory);

        setting = (TextView) view.findViewById(R.id.setting);
        about = (TextView) view.findViewById(R.id.about);
        logoutButton = (TextView) view.findViewById(R.id.logoutButton);

        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));


        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> callGetUser = userInterface.getUserAccount(database.getToken());

        callGetUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                userData = new Gson().toJsonTree(response.body()).getAsJsonObject().get("user").getAsJsonObject();
                if (response.isSuccessful()){
                    userNameView.setText(userData.get("name").getAsString());
                    userPhoneView.setText(userData.get("username").getAsString());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        getLiveLocation.setOnClickListener(view1 -> {
            getCurrentLocation();
        });



        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(), getChildFragmentManager().toString(), Toast.LENGTH_SHORT).show();

                database.setToken(null);
                startActivity(new Intent(view.getContext(), About.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(), getChildFragmentManager().toString(), Toast.LENGTH_SHORT).show();

                database.setToken(null);
                startActivity(new Intent(view.getContext(), Navigate.class));
                getActivity().finish();
            }
        });


        bloodDonnerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BloodDonnerForm.class));
            }
        });

        admitToHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), HospitalEnrollForm.class));
            }
        });

        patientHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), EnrollPatientHistory.class));
            }
        });

        return view;
    }

    private void getCurrentLocation(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> currentAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            userAddressView.setText(currentAddress.get(0).getAddressLine(0));
                            float[] range = new float[1];

                            Location userLocation = new Location("");
                            userLocation.setLatitude(26.466341);
                            userLocation.setLongitude(87.278315);
                            Location.distanceBetween(location.getLatitude(), location.getLongitude(),userLocation.getLatitude(), userLocation.getLongitude(),range);

                            Toast.makeText(getContext(),location.getLatitude()+" "+location.getLongitude()+"  "+range[0]/1000 , Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                getCurrentLocation();
            }else {
                Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}