package com.hydrogen.hospitalapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Activity.PaymentForEnroll;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsTab() {
        // Required empty public constructor
    }


    public static DetailsTab newInstance(String param1, String param2) {
        DetailsTab fragment = new DetailsTab();
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

    TextView paymentStatus, hideAndShowQr,sendData;
    LinearLayout qrCodeContainer;

    TextView payEnrolledDepartment;

    SharedPref sharedDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_details_tab, container, false);

        Intent intentData = getActivity().getIntent();
        JsonObject enrollObject = new JsonObject();
        enrollObject.addProperty("enroll_id", intentData.getIntExtra("enrollId", 0));



        paymentStatus = view.findViewById(R.id.paymentStatusCircleDetailsTab);
        hideAndShowQr = view.findViewById(R.id.hideAndShowQrDetailsTab);
        sendData = view.findViewById(R.id.sendDataToEmpDetailsTab);

        qrCodeContainer = view.findViewById(R.id.qrContainerLayoutDetailsTab);
        payEnrolledDepartment = view.findViewById(R.id.payButtonDetailsTab);


        sharedDatabase = new SharedPref(view.getContext());

        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> checkPaymentStatusByEnroll = userInterface.checkPaymentStatusByEnroll(sharedDatabase.getToken(), enrollObject);

        checkPaymentStatusByEnroll.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    boolean paymentStatusResult = new Gson().toJsonTree(response.body()).getAsJsonObject().get("payment_status").getAsBoolean();
                    if (paymentStatusResult){
                        paymentStatus.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.green));
                        payEnrolledDepartment.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


        payEnrolledDepartment.setOnClickListener(click -> {
            Intent in = new Intent(view.getContext(), PaymentForEnroll.class);
            in.putExtra("order_id",intentData.getIntExtra("order_id", 0));
            in.putExtra("product_name","enroll/"+intentData.getStringExtra("product_name"));
            in.putExtra("payAmount",intentData.getIntExtra("payAmount", 0));

            startActivity(in);
        });

        hideAndShowQr.setOnClickListener(click -> {
            toggleQrCode();
        });

        sendData.setOnClickListener(click ->{
            JsonObject enrollData = new JsonObject();
            enrollData.addProperty("emp_id",2);
            enrollData.addProperty("emp_name", "Neha Paudel");
            enrollData.addProperty("enroll_id", enrollObject.get("enroll_id").getAsInt());

            Call<Object> callSendData = userInterface.sendEnrollDataToEmployee(sharedDatabase.getToken(), enrollData);

            callSendData.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()){
                        boolean sendStatus = new Gson().toJsonTree(response.body()).getAsJsonObject().get("status").getAsBoolean();
                        if (sendStatus){
                            Toast.makeText(getContext(), "send successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        });

        return view;
    }

    private void toggleQrCode(){
        if (qrCodeContainer.getVisibility() == View.VISIBLE){
            qrCodeContainer.setVisibility(View.GONE);
        }else if (qrCodeContainer.getVisibility() == View.GONE){
            qrCodeContainer.setVisibility(View.VISIBLE);
        }


    }


}