package com.hydrogen.hospitalapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.Adapter.ContactListViewAdapter;
import com.hydrogen.hospitalapp.InterfaceAPI.UserInterface;
import com.hydrogen.hospitalapp.R;
import com.hydrogen.hospitalapp.RetrofitInstance;
import com.hydrogen.hospitalapp.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SnakeBiteContact extends AppCompatActivity {

    SharedPref sharedPref;
    UserInterface userInterface;
    List<JsonObject> dataList;

    ContactListViewAdapter contactListViewAdapter;

    RecyclerView snakeBiteContactRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_bite_contact);

        snakeBiteContactRecycler = findViewById(R.id.snakeBIteContactRecyclerView);
        snakeBiteContactRecycler.setLayoutManager(new LinearLayoutManager(this));

        sharedPref = new SharedPref(this);

        Retrofit retrofit = RetrofitInstance.getInstance(getResources().getString(R.string.ApiURL));

        userInterface = retrofit.create(UserInterface.class);

        Call<Object> getSnakeBiteContact = userInterface.getSnakeBiteContactList(sharedPref.getToken());


        getSnakeBiteContact.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Gson gson = new Gson();
                dataList = new ArrayList<>();
                if (response.isSuccessful()) {

                    JsonArray jsonArray = gson.toJsonTree(response.body()).getAsJsonObject().get("data").getAsJsonArray();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        dataList.add(jsonArray.get(i).getAsJsonObject());
                    }

                    System.out.println("data "+ dataList.get(1));

                    contactListViewAdapter = new ContactListViewAdapter(dataList, new ContactListViewAdapter.ContactViewListClick() {
                        @Override
                        public void onItemClick(JsonObject object) {

                            System.out.println(object);
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+object.get("contact").getAsString()));
                            startActivity(intent);

                        }
                    });
                }

                snakeBiteContactRecycler.setAdapter(contactListViewAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }
}