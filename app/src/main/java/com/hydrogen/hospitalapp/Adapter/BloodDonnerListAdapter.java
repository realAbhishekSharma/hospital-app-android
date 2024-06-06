package com.hydrogen.hospitalapp.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import java.util.List;

public class BloodDonnerListAdapter extends RecyclerView.Adapter<BloodDonnerListAdapter.ViewHolder> {

    List<JsonObject> donnerList;
    BloodDonnerItemClick bloodDonnerItemClick;

    public BloodDonnerListAdapter(List<JsonObject> donnerList, BloodDonnerItemClick bloodDonnerItemClick){
        this.donnerList = donnerList;
        this.bloodDonnerItemClick = bloodDonnerItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_doner_view_box, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameView.setText(donnerList.get(position).get("name").getAsString());
        holder.bloodGroup.setText(donnerList.get(position).get("blood_group").getAsString());
        holder.addressView.setText(donnerList.get(position).get("district").getAsString());

        holder.itemView.setOnClickListener(view -> {
            bloodDonnerItemClick.onItemClick(donnerList.get(position), 0);
        });

        holder.callDonner.setOnClickListener(view -> {
            bloodDonnerItemClick.onItemClick(donnerList.get(position), 1);
        });
    }

    @Override
    public int getItemCount() {
        return donnerList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, bloodGroup, addressView, callDonner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.donnerNameViewBox);
            bloodGroup = itemView.findViewById(R.id.bloodGroupViewBox);
            addressView = itemView.findViewById(R.id.donnerAddressViewBox);
            callDonner = itemView.findViewById(R.id.callDonnerBox);
        }
    }

    public interface BloodDonnerItemClick{
        void onItemClick(JsonObject object, int code);
    }
}
