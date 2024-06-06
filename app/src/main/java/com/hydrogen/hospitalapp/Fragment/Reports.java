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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.TestsViewBoxAdapter;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.SingleReportView;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reports extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reports() {
        // Required empty public constructor
    }


    public static Reports newInstance(String param1, String param2) {
        Reports fragment = new Reports();
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

    SharedPref sharedDatabase;
    List<JsonObject> testsDataList;

    RecyclerView testViewRecycler;

    TestsViewBoxAdapter testsViewBoxAdapter;

    TextView messageBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);


        messageBox = view.findViewById(R.id.messageReports);

        Intent intentData = getActivity().getIntent();
        JsonObject enrollObject = new JsonObject();
        enrollObject.addProperty("enroll_id", intentData.getIntExtra("enrollId", 0));
        System.out.println("enrollObject "+enrollObject);



        sharedDatabase = new SharedPref(view.getContext());

        testViewRecycler = view.findViewById(R.id.testViewRecyclerReports);
        testViewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));


        Retrofit retrofit  = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Object> callGetAllTest = userInterface.getAllTestByEnroll(sharedDatabase.getToken(),enrollObject);

        callGetAllTest.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.toJsonTree(response.body()).getAsJsonObject();
                    if (jsonObject.get("status").getAsBoolean()) {

                        messageBox.setVisibility(View.GONE);
                        testsDataList = new ArrayList<>();
                        JsonArray dataArray = jsonObject.get("data").getAsJsonArray();

                        for (JsonElement element : dataArray) {
                            testsDataList.add(element.getAsJsonObject());
                        }

                        testsViewBoxAdapter = new TestsViewBoxAdapter(testsDataList, new TestsViewBoxAdapter.TestViewBoxItemClick() {
                            @Override
                            public void onItemClick(JsonObject object) {

                                Intent i = new Intent(getContext(), SingleReportView.class);
                                i.putExtra("testForPatientId", object.get("test_for_patient_id").getAsInt());
                                i.putExtra("enrollId", object.get("enroll_id").getAsInt());
                                startActivity(i);
                            }
                        });
                        testViewRecycler.setAdapter(testsViewBoxAdapter);
                    }else {
                        messageBox.setVisibility(View.VISIBLE);
                        messageBox.setText(jsonObject.get("message").getAsString());
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                System.out.println("failed");

            }
        });


        return view;
    }
}