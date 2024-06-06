package com.hydrogen.hospitalapp.Adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import org.xml.sax.helpers.AttributesImpl;

import java.util.List;

public class NearByHospitalViewAdapter extends RecyclerView.Adapter<NearByHospitalViewAdapter.ViewHolder> {

    List<JsonObject> dataList;
    NearByHospitalClick nearByHospitalClick;

    public NearByHospitalViewAdapter(List<JsonObject> dataList, NearByHospitalClick nearByHospitalClick){
        this.dataList = dataList;
        this.nearByHospitalClick = nearByHospitalClick;
    }

    @NonNull
    @Override
    public NearByHospitalViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByHospitalViewAdapter.ViewHolder holder, int position) {

        holder.hospitalNameView.setText(dataList.get(position).get("hospital_name").getAsString());

        Location hospitalLocation = new Location("");
        Location userLocation = new Location("");
        userLocation.setLatitude(26.7944529);
        userLocation.setLongitude(87.2937926);
        hospitalLocation.setLatitude(Double.parseDouble(dataList.get(position).get("loc_latitude").getAsString()));
        hospitalLocation.setLongitude(Double.parseDouble(dataList.get(position).get("loc_longitude").getAsString()));

        holder.addressView.setText(dataList.get(position).get("district").getAsString());
        holder.distanceView.setText(userLocation.distanceTo(hospitalLocation)/1000+"km");

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalNameView, addressView, distanceView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalNameView = itemView.findViewById(R.id.titleView);
            addressView = itemView.findViewById(R.id.otherView);
            distanceView = itemView.findViewById(R.id.distanceView);


        }
    }

    public interface NearByHospitalClick{
        void onItemClick(JsonObject object);
    }

}
