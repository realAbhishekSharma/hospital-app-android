package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.hydrogen.hospitalapp.R;

public class ImportantContacts extends AppCompatActivity {

    TextView callButton, numberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_contacts);

        callButton = findViewById(R.id.callButtonImportantContact);
        numberView = findViewById(R.id.numberImportantContact);

        callButton.setOnClickListener(view -> {
            openCall();
        });
    }

    private void openCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+numberView.getText()));
        startActivity(intent);
    }
}