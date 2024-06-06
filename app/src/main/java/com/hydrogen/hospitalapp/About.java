package com.hydrogen.hospitalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    TextView teamMemberDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        teamMemberDetails = findViewById(R.id.teamMemberDetails);
        String details = "Abhishek Sharma\nGhanshyam Kharel\nAnish Gupta\nBarun Mandal";
        teamMemberDetails.setText(details);
    }
}