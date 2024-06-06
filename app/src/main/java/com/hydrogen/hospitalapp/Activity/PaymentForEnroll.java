package com.hydrogen.hospitalapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Fragment.MainDashboard;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PaymentForEnroll extends AppCompatActivity {

    TextView productNameView, amountView;
    KhaltiButton payKhaltiButton;

    String productName,productId;
    long payAmount;

    SharedPref sharedDatabase;
    UserInterface userInterface;

    JsonObject orderDetails;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_for_enroll);

        productNameView = (TextView) findViewById(R.id.productNameView);
        amountView = (TextView) findViewById(R.id.amountView);


        payKhaltiButton = (KhaltiButton) findViewById(R.id.payForEnroll);

        Intent i = getIntent();
        productId = i.getIntExtra("order_id",0)+"";
        productName = i.getStringExtra("product_name");
        payAmount = i.getIntExtra("payAmount",0);

        productNameView.setText(productName+productId);
        amountView.setText((float)payAmount/100+"");


        sharedDatabase = new SharedPref(this);

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        userInterface = retrofit.create(UserInterface.class);

        payKhaltiButton.setOnClickListener(view -> {
            openKhaltiPaymentMethod();
        });
    }

    private void openKhaltiPaymentMethod(){

        Config.Builder builder = new Config.Builder("test_public_key_4dec2b4f0e76448ebe48f99534d22044", productId, productName, payAmount, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
                orderDetails = new JsonObject();

                orderDetails.addProperty("order_id", productId);
                orderDetails.addProperty("payment_status", false);

                proceedPayment(orderDetails);

            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                orderDetails = new JsonObject();
                orderDetails.addProperty("order_id", productId);
                orderDetails.addProperty("transaction_id", data.get("idx").toString());
                orderDetails.addProperty("token", data.get("token").toString());
                orderDetails.addProperty("mobile_number", data.get("mobile").toString());
                orderDetails.addProperty("payment_status", true);

                System.out.println(orderDetails);
                proceedPayment(orderDetails);

            }
        }).paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                }});


        Config config = builder.build();

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);

        khaltiCheckOut.show();
    }


    private void proceedPayment(JsonObject data){
        Call<Object> savePaymentByOrder = userInterface.savePaymentByOrderId(sharedDatabase.getToken(), data);

        savePaymentByOrder.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                boolean paymentStatus = new Gson().toJsonTree(response.body()).getAsJsonObject().get("payment_status").getAsBoolean();
                if (response.isSuccessful()){
                    if (paymentStatus){
                        Toast.makeText(getApplicationContext(), "Payment successful.", Toast.LENGTH_SHORT).show();
                        goToMainDashboard();
                    }else {
                        Toast.makeText(getApplicationContext(), "Error on Payment.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private void goToMainDashboard(){
        startActivity(new Intent(getApplicationContext(), PatientDashboard.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        goToMainDashboard();
    }
}