package com.hydrogen.hospitalapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hydrogen.hospitalapp.R;

import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {

    private List<JsonObject> jsonList;
    private ItemClickListener itemClickListener;
    public SearchViewAdapter(List<JsonObject> jsonList, ItemClickListener itemClickListener){
        this.jsonList = jsonList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (jsonList.get(position).has("hospital_name")){
            holder.titleView.setText(jsonList.get(position).get("hospital_name").getAsString());
            holder.otherView.setText("Validity : "+jsonList.get(position).get("free_visit_limit").getAsString()+"days");
            holder.distanceView.setText("100m");

        }else if (jsonList.get(position).has("doctor_name")){
            holder.titleView.setText(jsonList.get(position).get("doctor_name").getAsString());
            holder.otherView.setVisibility(View.INVISIBLE);
            holder.distanceView.setVisibility(View.INVISIBLE);

        }else if (jsonList.get(position).has("service_name")){
            holder.titleView.setText(jsonList.get(position).get("service_name").getAsString());
//            holder.otherView.setText(jsonList.get(position).get("department_name").getAsString());
            holder.otherView.setText(jsonList.get(position).get("department_name").getAsString());
            holder.distanceView.setVisibility(View.INVISIBLE);
        }


        holder.itemView.setOnClickListener(view -> {
            itemClickListener.onItemClick(jsonList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public interface ItemClickListener{
        void onItemClick(JsonObject object);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView, otherView, distanceView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleView);
            otherView = itemView.findViewById(R.id.otherView);
            distanceView = itemView.findViewById(R.id.distanceView);
        }
    }
}
