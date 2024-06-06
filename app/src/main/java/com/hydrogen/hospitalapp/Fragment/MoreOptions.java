package com.hydrogen.hospitalapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hydrogen.hospitalapp.Activity.BloodDonnerList;
import com.hydrogen.hospitalapp.Activity.NearbyHospitals;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.Activity.SnakeBiteContact;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreOptions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreOptions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreOptions.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreOptions newInstance(String param1, String param2) {
        MoreOptions fragment = new MoreOptions();
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

    TextView snakeBite, ambulanceContact, searchBloodDonner,nearbyHospitals, testHealth, testSleepQuality;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_more_options, container, false);

        snakeBite = view.findViewById(R.id.snakeBIteContact);
        ambulanceContact = view.findViewById(R.id.ambulanceContact);
        searchBloodDonner = view.findViewById(R.id.searchBloodDonner);
        nearbyHospitals = view.findViewById(R.id.nearbyHospitals);
        testHealth = view.findViewById(R.id.testHealth);
        testSleepQuality = view.findViewById(R.id.testSleepQuality);

        snakeBite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( view.getContext(), SnakeBiteContact.class);
                startActivity(intent);
            }
        });

        searchBloodDonner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( view.getContext(), BloodDonnerList.class);
                startActivity(intent);
            }
        });

        nearbyHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( view.getContext(), NearbyHospitals.class);
                startActivity(intent);
            }
        });

        return view;
    }
}