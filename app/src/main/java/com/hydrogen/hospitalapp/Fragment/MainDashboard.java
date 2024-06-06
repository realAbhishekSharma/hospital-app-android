package com.hydrogen.hospitalapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Activity.DepartmentEnrollForm;
import com.hydrogen.hospitalapp.Activity.Navigate;
import com.hydrogen.hospitalapp.Adapter.EnrollPreviewAdapter;
import com.hydrogen.hospitalapp.Activity.EnrolledDepartmentDetails;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDashboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static MainDashboard newInstance(String param1, String param2) {
        MainDashboard fragment = new MainDashboard();
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

    TextView titleView, hospitalName, addEnroll;
    SharedPref sharedDatabase;
    List<JsonObject> enrollDataList;
    RecyclerView enrollRecyclerView;

    EnrollPreviewAdapter enrollPreviewAdapter;
    JsonObject dataObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        sharedDatabase = new SharedPref(view.getContext());

        titleView = (TextView) view.findViewById(R.id.titleView);
        hospitalName = (TextView) view.findViewById(R.id.hospitalName);
        addEnroll = (TextView) view.findViewById(R.id.addEnroll);
        addEnroll.setVisibility(View.GONE);

        enrollRecyclerView = view.findViewById(R.id.enrollRecyclerView);
        enrollRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String token = sharedDatabase.getToken();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.ApiURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);

        JsonObject patientObject = new JsonObject();

        Call<Object> callActivePatient = userInterface.getActivePatientByUser(token);

        callActivePatient.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                    dataObject = jsonObject.get("data").getAsJsonObject();


                    titleView.setText(dataObject.get("title").getAsString());
                    hospitalName.setText(dataObject.get("hospital_name").getAsString());

                    addEnroll.setVisibility(View.VISIBLE);

                    patientObject.addProperty("patient_id",dataObject.get("id").getAsInt());


                    Call<Object> callGetEnrollByPatient = userInterface.getEnrollByPatient(token,patientObject);
                    callGetEnrollByPatient.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            String finalValue;

                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                            enrollDataList = new ArrayList<>();

                            if (response.isSuccessful()){
                                JsonArray dataArray = jsonObject.get("data").getAsJsonArray();

                                for (JsonElement element : dataArray) {
                                    enrollDataList.add(element.getAsJsonObject());
                                }

                                enrollPreviewAdapter = new EnrollPreviewAdapter(getContext(),enrollDataList, new EnrollPreviewAdapter.EnrollItemClick() {
                                    @Override
                                    public void onItemClick(JsonObject object) {

                                        Intent i = new Intent(getContext(), EnrolledDepartmentDetails.class);
                                        i.putExtra("order_id", object.get("order_id").getAsInt());
                                        i.putExtra("enrollId", object.get("enroll_id").getAsInt());
                                        i.putExtra("product_name", dataObject.get("title").getAsString());
                                        i.putExtra("payAmount", dataObject.get("opd_charge").getAsInt());
                                        startActivity(i);
                                    }
                                });
                                enrollRecyclerView.setAdapter(enrollPreviewAdapter);

                                System.out.println(enrollDataList);
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                }

                if (response.code() == 403){
                    sharedDatabase.setToken(null);
                    startActivity(new Intent(getActivity().getApplicationContext(), Navigate.class));
                    getActivity().finish();
                    Toast.makeText(view.getContext(), "Session expired.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        addEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DepartmentEnrollForm.class);
                i.putExtra("patient" , String.valueOf(dataObject));
                startActivity(i);

            }
        });

//        Toast.makeText(getActivity().getApplicationContext(),sharedPreferences.getString(TOKEN, null), Toast.LENGTH_SHORT).show();

        return view;
    }
}