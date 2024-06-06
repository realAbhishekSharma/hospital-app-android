package com.hydrogen.hospitalapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hydrogen.hospitalapp.Fragment.Account;
import com.hydrogen.hospitalapp.Fragment.MainDashboard;
import com.hydrogen.hospitalapp.Fragment.Medicine;
import com.hydrogen.hospitalapp.Fragment.MoreOptions;
import com.hydrogen.hospitalapp.Fragment.Reports;
import com.hydrogen.hospitalapp.R;

public class PatientDashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.patientBottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.patientContainerFrame, new MainDashboard()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment newFragment = null;
                switch (item.getItemId()){
                    case R.id.dashboardMenu:
                        newFragment = new MainDashboard();
                        break;
//                    case R.id.reportMenu:
//                        newFragment = new Reports();
//                        break;
                    case R.id.medicineMenu:
                        newFragment = new Medicine();
                        break;
                    case R.id.accountMenu:
                        newFragment = new Account();
                        break;
                    case R.id.moreMenu:
                        newFragment = new MoreOptions();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.patientContainerFrame, newFragment).commit();

                return true;
            }
        });
    }
}